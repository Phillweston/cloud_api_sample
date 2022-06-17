## Docker 安装

  - 安装教程：[https://docs.docker.com/engine/install/ubuntu/](https://docs.docker.com/engine/install/ubuntu/)

## Docker Compose 安装

  - 安装教程：[https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)

## 源码包下载

- 点击下载 [cloud_api_sample_docker_1.0.0.zip.](https://terra-sz-hc1pro-cloudapi.oss-cn-shenzhen.aliyuncs.com/c0af9fe0d7eb4f35a8fe5b695e4d0b96/docker/cloud_api_sample_docker_1.0.0.zip)

## 解压文件

将 cloud_api_sample_docker_1.0.0.zip 文件解压后目录结构如下：

![image-20220321112952651](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/195959b3-f8e1-4f3d-9d9b-d90ece297e15.png)

- data
  存放demo服务运行的用户数据

- docker-compose.yml
  docker-compose的运行配置文件

- docs
  存放各类文档，包括API文档

- source
  存放源代码，各类镜像的源文件

- cloud_api_sample_docker_v1.0.0.tar
  所有环境的 docker 镜像

- README.md

- update_backend.sh

  构建后端镜像文件

- update_front.sh

  构建前端镜像文件

## 载入镜像

使用 docker load 命令则可将这个镜像文件载入进来。	

```shell
  sudo docker load < cloud_api_sample_docker_v1.0.0.tar
```

- 进入source/backend_service/src/main/resources 下，修改后台配置文件“application.yml“，修改配置文件中的mysql配置、mqtt配置、以及对象存储服务器配置。

- 进入source/nginx/front_page/src/api/http 下，修改前端配置文件”config.ts“，填上在开发者网站申请的appId，appKey 和 appLicense，请看[申请教程](https://developer.dji.com/cn/document/209883f1-f2ad-406e-b99c-be7498df7f10)。

  *注意：如果不使用直播功能，只需要先设置baseURL 和 websocketURL。如果使用地图还需要去高德地图官网申请amapKey。*

  *注意：除了这两个配置文件，其他的东西暂时都不用改，直接构建然后启动项目尝试。*

- **<font size="4">进入update_front.sh 文件的目录下，构建前后端镜像</font>**

  ```shell
   # 构建前端镜像
   ./update_front.sh
   
   # 构建后端镜像
   ./update_backend.sh
  ```


## 启动容器

进入 docker-compose.yml 文件的路径下，使用 docker-compose 将所有的镜像启动

```shell
  sudo docker-compose up -d
```

## Pilot 2登录程序

1. 打开 pilot 2，进入主页面，点击云服务进入。

   ​	![GTScreenshot_20220321_232931.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/c47f8289-1331-4433-b655-39028637080e.png)

2. 选择右下角的开放平台。

   ​	![GTScreenshot_20220321_233150.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/16084fab-bca5-458a-a7e8-987cbc901e52.png)

3. 输入前端访问地址（默认地址：http://ip:8080/pilot-login)后，点击右上角的**连接**按钮进入。

   ​	![GTScreenshot_20220321_233344.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/48383fc8-f039-4403-a40f-6c8dfb3a1248.png)

4. 账户名：pilot，密码：pilot123，点击 **Login** 按钮登录。

   ​	![GTScreenshot_20220321_233736.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/76990178-c000-478b-ba45-2a57db8756fb.png)

5. 如果主页面显示 Connected，说明已经登录成功，遥控器已经连接上 mqtt 服务器，并且开始推送数据。现在demo 就已经跑起来了，你可以点击遥控器上的返回按钮返回主页面了，只要不点击右上角的**退出**按钮，你就仍然处于登录状态。

   ​	![GTScreenshot_20220321_233859.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/62c30970-8eeb-4732-8868-787404773b2a.png)

6. 你已经可以在主页面看到工作空间的信息了，只要字体是深黑色，说明你依旧处于登录状态，遥控器以及飞机的数据会持续的推送中。如果想要退出工作空间，只需要再次点击进入，然后点击右上角的**退出**按钮就可以退出了，遥控器和飞机就不会再推送数据了。

   ​	![GTScreenshot_20220321_234607.png](https://stag-terra-1-g.djicdn.com/7774da665e07453698314cc27c523096/admin/doc/c37599fb-bcdd-4ef8-90ee-8d1e8f1e9c77.png)

   

## Web端登录程序

登录页面默认地址：http://ip:8080/project

账户名：adminPC

密码：adminPC
