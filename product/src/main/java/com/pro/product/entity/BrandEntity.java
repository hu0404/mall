package com.pro.product.entity;

import com.aliyuncs.ram.model.v20150501.UpdateGroupRequest;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.pro.common.exception.groups.AddGroupsInterface;
import com.pro.common.valid.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author hwt
 * @email 735325237@qq.com
 * @date 2023-11-25 11:28:19
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "更新数据品牌ID必须不为空", groups = {AddGroupsInterface.class})
	@Null(message = "更新数据品牌ID必须不为空", groups = {UpdateGroupRequest.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	//@NotEmpty
	//@NotNull
	@NotBlank(message = "品牌的名称不能为空")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "logo不能为空")
	@URL(message = "logo必须是一个合法URL地址")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue( val= {0,1} )
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "检索首字母不能为空")
//	@Pattern(regexp = "/^[a-zA-Z]$/",message = "检索首字母必须是单个的字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为null")
	@Min(value = 0,message = "排序不能小于0")
	private Integer sort;

}
