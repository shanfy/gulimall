package com.yang.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.gulimall.product.entity.BrandEntity;
import com.yang.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

// @RunWith(SpringRunner.class)
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStringRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        //保存
        ops.set("hello","world_" + UUID.randomUUID().toString());

        //查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据:"+hello);
    }

   /* @Autowired(required = false)
    private OSSClient ossClient;

    @Test
    void testUploadClient() throws FileNotFoundException {
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "shanfy-gulimall";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "testupload/手机.jpg";
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\java学习资料\\Java大型电商项目 谷粒商城\\1.分布式基础（全栈开发篇）\\课件和文档\\基础篇\\资料\\pics\\0d40c24b264aa511.jpg";
        InputStream inputStream = new FileInputStream(filePath);
        // download file to local
        ossClient.putObject(bucketName, objectName,inputStream);
        System.out.println("上传成功");
    }*/
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        /*brandEntity.setName("华为");
        brandService.save(brandEntity);*/

 /*       brandEntity.setBrandId(1L);
        brandEntity.setDescript("国产一哥");
        brandService.updateById(brandEntity);*/

        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id",1L);
        List<BrandEntity> list = brandService.list(wrapper);
        System.out.println(list.toString());

    }

   /* @Test
     void updateAliOSS() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t9RhjFyyv8mGPuVTQaq";
        String accessKeySecret = "s3ghQHcspjpmnueqXSUJLgNnHOiE3y";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "shanfy-gulimall";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "testupload/手机.jpg";
// 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= "D:\\java学习资料\\Java大型电商项目 谷粒商城\\1.分布式基础（全栈开发篇）\\课件和文档\\基础篇\\资料\\pics\\0d40c24b264aa511.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
            System.out.println("上传完成");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }*/

}
