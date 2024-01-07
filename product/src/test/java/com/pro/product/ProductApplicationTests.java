package com.pro.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pro.product.entity.BrandEntity;
import com.pro.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest(classes = ProductApplication.class)
class ProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity entity = new BrandEntity();
        entity.setName("魅族");
        brandService.save(entity);
    }

    @Test
    void selectAll() {
        List<BrandEntity> list = brandService.list();
        for (BrandEntity entity : list) {
            System.out.println(entity);
        }
    }

    @Test
    void selectById() {
        List<BrandEntity> list = brandService
                .list(new QueryWrapper<BrandEntity>().eq("brand_id",2));
        for (BrandEntity entity : list) {
            System.out.println(entity);
        }
    }

    @Autowired
    private OSSClient ossClient;

    @Test
    public void testUploadFile() throws Exception {

        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("E:\\5.png");
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。

        ossClient.putObject("huwentao-mall","6.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("成功...");
    }

    private static ExecutorService service = Executors.newFixedThreadPool(5);
    @Test
    void selectById111() {
        service.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5
            ,50
            ,10
            , TimeUnit.SECONDS
            ,new LinkedBlockingQueue<>(100)
            , Executors.defaultThreadFactory()
            ,new ThreadPoolExecutor.AbortPolicy()
    );
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程main"+Thread.currentThread().getName());
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    System.out.println("线程1开始了..."+Thread.currentThread().getName());
                    int i = 100 / 50;

                    return i;
                }, executor).whenCompleteAsync((res,exception)-> System.out.println(res +"ForkJoinPool.commonPool-worker-1开始了"+exception+Thread.currentThread().getName()))
                .exceptionally(res->{
                    System.out.println("res="+res+Thread.currentThread().getName());
                    return 10;
                });
        System.out.println("获取的线程的返回结果是：" + future.get() );
    }
}
