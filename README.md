# web3j

## 安装依赖

### 1、安装Linux环境的java：

以jdk1.8.0_144为例：

  1）到[JAVA官网](http://www.oracle.com/technetwork/java/javase/downloads/index.html)，下载 jdk-8u144-linux-x64.tar.gz
    
  2）将安装包拷贝到指定安装目录（例如/usr），然后解压缩 

    tar -zxcf jdk-8u144-linux-x64.tar.gz
    
  3）配置环境变量，在 /etc/profile中 增加

    export JAVA_HOME=/usr/jdk1.8.0_144
    export JRE_HOME=$JAVA_HOME/jre
    export CLASSPATH=.:$CLASSPATH:$JAVA_HOME/lib:$JRE_HOME/lib
    export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
       
  4）验证是否安装成功
 
    java -version  

### 2、安装gradle

  使用包管理器安装：
   
  1）使用包管理器 [SDKMAN!](http://sdkman.io/) 安装，该方式不仅安装简单，而且无需用户自动配置环境变量。安装包管理器 sdk，其命令如下：
     
    curl -s "https://get.sdkman.io" | bash

   采用以下命令使sdk的配置生效：
   
    source "$HOME/.sdkman/bin/sdkman-init.sh"

   验证是否安装成功：
   
    sdk version

  2）利用sdk安装gradle 4.1版本（该版本是最新版本），其命令如下：
   
    sdk install gradle 4.1

  3）验证gradle是否安装成功
  
    gradle -v

   手动安装：
   
  1）下载安装包
  
    wget https://services.gradle.org/distributions/gradle-4.1-bin.zip

   解压安装包
   
    unzip gradle-4.1-bin.zip

   根据用户需要，将安装包放到指定安装目录
   
    mv gradle-4.1 /usr/local/

  2）配置环境变量
   vi /etc/profile ， 在文件末尾添加以下内容：
   
    export GRADLE_HOME=/usr/local/gradle-4.1
    PATH=$PATH:$GRADLE_HOME/bin

   使配置生效
   
    source /etc/profile

  3）验证gradle是否安装成功
  
    gradle -v

## web3j简介

web3j发送交易的方式是通过封装 JSON-PRC API 来实现的，web3j提供了支持http和IPC的通信方式，目前的改造仅支持http的方式发送CITA交易。
发送交易成功后，web3j还对JSON-RPC的响应结果进行了封装，针对不同的交易结果设置了不同的响应对象。

### 1、发送交易请求

   具体参考 web3j/src/main/java/org/web3j/welcome/Main.java 中main函数中的示例。
   
   1）发送同步交易请求

    web3j = Web3j.build(new HttpService("http://localhost:1337/"));  	
    EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
    int num = ethBlockNumber.getBlockNumber();

   2）发送异步交易请求

    web3j = Web3j.build(new HttpService("http://localhost:1337/")); 	
    EthBlockNumber ethBlockNumber = web3j.ethBlockNumber()..sendAsync().get();
    int num = ethBlockNumber.getBlockNumber();

### 2、构造CITA的交易结构

   具体参考 web3j/src/main/java/org/web3j/welcome/Main.java 中testProtobuf函数中的示例。
   因为CITA新版已经更改了HASH方法和加密方式，该功能还需进一步完善。

### 3、运行Main函数中的测试案例

   使用命令 gradle run 执行，便可以获得运行结果


## [TODO]

### 1、web3j中调用Solidity智能合约

   1）web3j提供了一个可以自动生成智能合约java代码的方式，利用该代码包既可以部署合约，也可以调用合约。
   具体可以参考 [web3j智能合约调用](https://docs.web3j.io/smart_contracts.html#smart-contract-wrappers)

   2）web3j提供了一个命令行工具 web3j ，该命令可以支持Linux和windows，可以到web3j的 [release版本](https://docs.web3j.io/command_line.html)
   下载  web3j-2.3.0.zip ，解压之后便可以使用。web3j的作用如下：
   
    1）钱包创建
    2）钱包和密码管理
    3）交易钱包账户的资金
    4）生成智能合约功能的java代码

   3）在生成wrapper包之前，需要编译智能合约，命令如下：（没有下载solc编译器的需要去Solidity官网下载）
   
    solc <contract>.sol --bin --abi --optimize -o <output-dir>/
    
   该命令将编译Solidity智能合约生成的.bin文件和.abi文件保存到指定目录

   4）生成java代码包的命令为：

    web3j solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

   其中生成的java文件目录为 /path/to/src/main/java/com/your/organisation/name/<smart-contract>.java

   5）除了使用命令行工具外，也可以通过web3j的源代码进行操作，其作用效果是一样的，命令如下：
	
    org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

   6）将生成的智能合约代码包放到项目中，便可以通过生成的java函数部署（创建）智能合约了，如果部署成功就能对智能合约进行调用。(暂未完成)
   部署智能合约的参考示例如下：

    Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
    //以太坊钱包的秘钥，Credentials是用户账户的私钥和公钥对（新版CITA公钥和私钥长度与现有的不一致，待更新）
    Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   部署合约，得到创建合约后的对象。该创建合约的调用方式，最终采用发送字节化交易的形式（ethSendRawTransaction，对应CITA的cita_sendTransaction）发送出去，所以为了获得字节化的原始交易数据，所以需要重新对CITA中的protobuf结构的交易进行构造（暂未完成）
    
    YourSmartContract contract = YourSmartContract.deploy(
        <web3j>, <credentials>,
        GAS_PRICE, GAS_LIMIT,
        <initialEtherValue>,
        <param1>, ..., <paramN>).get();  // constructor params

   利用存在的合约地址，构造合约对象
   
    YourSmartContract contract = YourSmartContract.load(
        "0x<address>", <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT);

   调用智能合约的参考示例如下：
   
    Type result = contract.someMethod(
             new Type(...),
             ...).get();


### 2、构造protobuf结构的cita交易 

   1）通过 [protobuf官网](https://github.com/google/protobuf/releases) 下载编译proto文件的命令行工具protoc(java版)，其编译命令如下：
    
    protoc --java_out=$outpath -I=$inputpath $inputpath/descriptor.proto

   2）通过protoc构建 user.proto 生成对应的 user.java：
    
    protoc --java_out=$outpath -I=$inputpath $inputpath/user.proto
    
   其中 $inputpath 表示 proto文件所在的路径，$outpath表示存放生成的java文件的路径。

   3）构造CITA的交易结构，该功能需将CITA新版的HASH方法和加密方式构建java的形式后才可继续进行。









