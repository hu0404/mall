package com.pro.product.service.impl;

import com.pro.product.service.CategoryBrandRelationService;
import com.pro.product.vo.Catalog2VO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.utils.PageUtils;
import com.pro.common.utils.Query;

import com.pro.product.dao.CategoryDao;
import com.pro.product.entity.CategoryEntity;
import com.pro.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> queryPageWithTree(Map<String, Object> params) {
        // 1.查询所有的商品分类信息
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 2.将商品分类信息拆解为树形结构【父子关系】
        // 第一步遍历出所有的大类  parent_cid = 0
        List<CategoryEntity> categoryList = categoryEntities.stream()
                .filter(categoryEntitie -> categoryEntitie.getParentCid() == 0)
                .map(categoryEntitieParent -> {
                    // 根据大类找到多有的小类  递归的方式实现
                    categoryEntitieParent.setChildrens(getCatogeryChirldrens(categoryEntitieParent, categoryEntities));
                    return categoryEntitieParent;
                })
                .sorted((entity1, entity2) -> (entity1.getSort() == null ? 0 : entity1.getSort()) - (entity2.getSort() == null ? 0 : entity2.getSort()))
                .collect(Collectors.toList());
        return categoryList;
    }

    private List<CategoryEntity> getCatogeryChirldrens(CategoryEntity categoryEntitieParent, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> list = categoryEntities.stream()
                .filter(entity -> entity.getParentCid().equals(categoryEntitieParent.getCatId()))
                .map(entitieParent -> {
                    // 根据大类找到多有的小类  递归的方式实现
                    entitieParent.setChildrens(getCatogeryChirldrens(entitieParent, categoryEntities));
                    return entitieParent;
                }).sorted((entity1, entity2) -> (entity1.getSort() == null ? 0 : entity1.getSort()) - (entity2.getSort() == null ? 0 : entity2.getSort()))
                .collect(Collectors.toList());
        return list;
    }



    @Override
    public void removeCategoryByIds(List<Long> ids) {
        // TODO  1.检查类别数据是否在其他业务中使用

        // 2.批量逻辑删除操作
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void updateDetail(CategoryEntity category) {
        //1.更新数据源
        this.updateById(category);
        if(!StringUtils.isEmpty(category.getName())){
            //同步更新级联关系中的数据
            categoryBrandRelationService.updateCatelogName(category.getCatId(),category.getName());
        }
    }

    /**
     * 查询出所有的商品大类(一级分类)
     *    在注解中我们可以指定对应的缓存的名称，起到一个分区的作用，一般按照业务来区分
     *    @Cacheable({"catagory","product"}) 代表当前的方法的返回结果是需要缓存的，
     *                                       调用该方法的时候，如果缓存中有数据，那么该方法就不会执行，
     *                                       如果缓存中没有数据，那么就执行该方法并且把查询的结果缓存起来
     *    缓存处理
     *       1.存储在Redis中的缓存数据的Key是默认生成的：缓存名称::SimpleKey[]
     *       2.默认缓存的数据的过期时间是-1永久
     *       3.缓存的数据，默认使用的是jdk的序列化机制
     *    改进：
     *       1.生成的缓存数据我们需要指定自定义的key： key属性来指定，可以直接字符串定义也可以通过SPEL表达式处理：#root.method.name
     *       2.指定缓存数据的存活时间: spring.cache.redis.time-to-live 指定过期时间
     *       3.把缓存的数据保存为JSON数据
     *   SpringCache的原理
     *     CacheAutoConfiguration--》根据指定的spring.cache.type=reids会导入 RedisCacheAutoConfiguration
     * @return
     */
//    @Trace
    @Cacheable(value = {"catagory"},key = "#root.method.name",sync = true)
    @Override
    public List<CategoryEntity> getLeve1Category() {
        System.out.println("查询了数据库操作....");
        long start = System.currentTimeMillis();
        List<CategoryEntity> list = baseMapper.queryLeve1Category();
        System.out.println("查询消耗的时间:" + (System.currentTimeMillis() - start));
        return list;
    }




    /**
     * 查询出所有的二级和三级分类的数据
     * 并封装为Map<String, Catalog2VO>对象
     * @return
     */
    @Override
    public Map<String, List<Catalog2VO>> getCatelog2JSON() {
        // 获取所有的一级分类的数据
        List<CategoryEntity> leve1Category = this.getLeve1Category();
        // 把一级分类的数据转换为Map容器 key就是一级分类的编号， value就是一级分类对应的二级分类的数据
        Map<String, List<Catalog2VO>> map = leve1Category.stream().collect(Collectors.toMap(
                key -> key.getCatId().toString()
                , value -> {
                    // 根据一级分类的编号，查询出对应的二级分类的数据
                    List<CategoryEntity> l2Catalogs = baseMapper
                            .selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", value.getCatId()));
                    List<Catalog2VO> Catalog2VOs =null;
                    if(l2Catalogs != null){
                        Catalog2VOs = l2Catalogs.stream().map(l2 -> {
                            // 需要把查询出来的二级分类的数据填充到对应的Catelog2VO中
                            Catalog2VO catalog2VO = new Catalog2VO(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 根据二级分类的数据找到对应的三级分类的信息
                            List<CategoryEntity> l3Catelogs = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", catalog2VO.getId()));
                            if(l3Catelogs != null){
                                // 获取到的二级分类对应的三级分类的数据
                                List<Catalog2VO.Catalog3VO> catalog3VOS = l3Catelogs.stream().map(l3 -> {
                                    Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO(l3.getParentCid().toString(), l3.getCatId().toString(), l3.getName());
                                    return catalog3VO;
                                }).collect(Collectors.toList());
                                // 三级分类关联二级分类
                                catalog2VO.setCatalog3List(catalog3VOS);
                            }
                            return catalog2VO;
                        }).collect(Collectors.toList());
                    }

                    return Catalog2VOs;
                }
        ));
        return map;
    }

    /**
     * 跟进父编号获取对应的子菜单信息
     * @param list
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> queryByParenCid(List<CategoryEntity> list,Long parentCid){
        List<CategoryEntity> collect = list.stream().filter(item -> {
            return item.getParentCid().equals(parentCid);
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查询出所有的二级和三级分类的数据
     * 并封装为Map<String, Catalog2VO>对象
     * @return
     */
    @Override
    public Map<String, List<Catalog2VO>> getCatelog2JSONyouhua() {
        // 获取所有的分类数据
        List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>());
        // 获取所有的一级分类的数据
        List<CategoryEntity> leve1Category = this.queryByParenCid(list,0l);
        // 把一级分类的数据转换为Map容器 key就是一级分类的编号， value就是一级分类对应的二级分类的数据
        Map<String, List<Catalog2VO>> map = leve1Category.stream().collect(Collectors.toMap(
                key -> key.getCatId().toString()
                , value -> {
                    // 根据一级分类的编号，查询出对应的二级分类的数据
                    List<CategoryEntity> l2Catalogs = this.queryByParenCid(list,value.getCatId());
                    List<Catalog2VO> Catalog2VOs =null;
                    if(l2Catalogs != null){
                        Catalog2VOs = l2Catalogs.stream().map(l2 -> {
                            // 需要把查询出来的二级分类的数据填充到对应的Catelog2VO中
                            Catalog2VO catalog2VO = new Catalog2VO(l2.getParentCid().toString(), null, l2.getCatId().toString(), l2.getName());
                            // 根据二级分类的数据找到对应的三级分类的信息
                            List<CategoryEntity> l3Catelogs = this.queryByParenCid(list,l2.getCatId());
                            if(l3Catelogs != null){
                                // 获取到的二级分类对应的三级分类的数据
                                List<Catalog2VO.Catalog3VO> catalog3VOS = l3Catelogs.stream().map(l3 -> {
                                    Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO(l3.getParentCid().toString(), l3.getCatId().toString(), l3.getName());
                                    return catalog3VO;
                                }).collect(Collectors.toList());
                                // 三级分类关联二级分类
                                catalog2VO.setCatalog3List(catalog3VOS);
                            }
                            return catalog2VO;
                        }).collect(Collectors.toList());
                    }

                    return Catalog2VOs;
                }
        ));
        return map;
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        ArrayList<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId,paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);

    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        paths.add(catelogId);
        CategoryEntity entity = this.getById(catelogId);
        if(entity.getParentCid() != 0){
            findParentPath(entity.getParentCid(),paths);
        }
        return paths;
    }
}
