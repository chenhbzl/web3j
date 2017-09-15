package org.web3j.welcome;

import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.request.Call;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;

import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Function;

import org.web3j.crypto.Credentials;
import java.util.Random;


/**
 * Demonstrations of working with RxJava's Observables in web3j.
 */
public class Main {

    private final Web3j web3j;

    public Main() {
        web3j = Web3j.build(new HttpService("http://localhost:1337/"));  // defaults to http://localhost:8545/
    }

    private void run() throws Exception {
        //despite whether String the prefix is 0x or not, the result is right

        System.out.println("======================================");
        System.out.println("***  1.  net_peerCount             ***");
        testNetPeerCount();

        System.out.println("======================================");
        System.out.println("***  2.  cita_blockNumber          ***");
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        BigInteger validBlockNumber;
        if(ethBlockNumber.isEmpty() == true){
            System.out.println("the result is null");
            return;
        } else {
            System.out.println("cita_blockNumber:" + ethBlockNumber.getBlockNumber());
            validBlockNumber = ethBlockNumber.getBlockNumber();
        }

        System.out.println("======================================");
        System.out.println("***  3.  cita_getBlockByNumber     ***");
        boolean isfullTranobj = true;
        String validBlockHash;
        EthBlock ethBlock = web3j.ethGetBlockByNumber(
            DefaultBlockParameter.valueOf(validBlockNumber), isfullTranobj).send();

        if(ethBlock.isEmpty() == true){
            System.out.println("the result is null");
            return;
        }
        else{
            EthBlock.Block block = ethBlock.getBlock();
            System.out.println("hash(blockhash):" + block.getHash());
            validBlockHash = block.getHash();
            System.out.println("version:" + block.getVersion());
            //System.out.println("header.timestamp:" + block.getHeader().getTimeStamp());
            System.out.println("header.prevHash:" + block.getHeader().getPrevHash());
            System.out.println("header.number(hex):" + block.getHeader().getNumber());
            System.out.println("header.number(dec):" + block.getHeader().getNumberDec());
            System.out.println("header.stateRoot:" + block.getHeader().getStateRoot());
            System.out.println("header.transactionsRoot:" + block.getHeader().getTransactionsRoot());
            System.out.println("header.receiptsRoot:" + block.getHeader().getReceiptsRoot());
            System.out.println("header.gasUsed:" + block.getHeader().getGasUsed());
            System.out.println("header.proof.proposal:" + block.getHeader().getProof().getProposal());
            System.out.println("header.proof.height:" + block.getHeader().getProof().getHeight());
            System.out.println("header.proof.round:" + block.getHeader().getProof().getRound());

            if(!block.getBody().getTransactions().isEmpty()) {
                System.out.println("number of transaction:" + block.getBody().getTransactions().size());

                int i;
                for(i=0; i < block.getBody().getTransactions().size(); i++ ){
                    org.web3j.protocol.core.methods.response.Transaction tran = (org.web3j.protocol.core.methods.response.Transaction) block.getBody().getTransactions().get(i).get();
                    System.out.println("body.transactions.tranhash:" + tran.getHash());
                    System.out.println("body.transactions.content:" + tran.getContent());
                }
            }else{
                System.out.println("the block transactions is null");
            }
        }

        System.out.println("======================================");
        System.out.println("***  4.  cita_getBlockByHash       ***");
        boolean isfullTran = true;
        testEthGetBlockByHashReturnFullTransactionObjects(validBlockHash, isfullTran);

        System.out.println("======================================");
        System.out.println("***  5.  cita_sendTransaction      ***");
        String code = "6060604052341561000f57600080fd5b5b6103558061001f6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635537f99e1461005f57806360fe47b1146100bc5780636d4ce63c146100df578063e2f5cc0d14610108575b600080fd5b341561006a57600080fd5b6100ba600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610197565b005b34156100c757600080fd5b6100dd60048080359060200190919050506101b2565b005b34156100ea57600080fd5b6100f26101bd565b6040518082815260200191505060405180910390f35b341561011357600080fd5b61011b6101c7565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561015c5780820151818401525b602081019050610140565b50505050905090810190601f1680156101895780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b80600190805190602001906101ad929190610270565b505b50565b806000819055505b50565b6000805490505b90565b6101cf6102f0565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102655780601f1061023a57610100808354040283529160200191610265565b820191906000526020600020905b81548152906001019060200180831161024857829003601f168201915b505050505090505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102b157805160ff19168380011785556102df565b828001600101855582156102df579182015b828111156102de5782518255916020019190600101906102c3565b5b5090506102ec9190610304565b5090565b602060405190810160405280600081525090565b61032691905b8082111561032257600081600090555060010161030a565b5090565b905600a165627a7a7230582059b58c613b38589c882547b0ac070977b9e3fc6629d05bc219ede6de115470590029";
        Random r = new Random(System.currentTimeMillis());
        long nonce = Math.abs(r.nextLong());
        System.out.println("nonce = " + nonce);
        Transaction rtx = Transaction.createContractTransaction(BigInteger.valueOf(nonce), 1000000, validBlockNumber.longValue() + 88, code);
        String privateKey = "80762b900f072d199e35ea9b1ee0e2e631a87762f8855b32d4ec13e37a3a65c1";
        EthSendTransaction ethSendTx =  web3j.ethSendRawTransaction(rtx.sign(privateKey)).send();
        if(ethSendTx.isEmpty() == true){
            System.out.println("the result is null");
            return;
        }
        else {
            System.out.println("hash(Transaction):" + ethSendTx.getSendTransactionReult().getHash());
            System.out.println("status:" + ethSendTx.getSendTransactionReult().getStatus());
        }

        System.out.println("waiting for tx into chain ...");
        Thread.sleep(8000);

        System.out.println("======================================");
        System.out.println("***  6.  cita_getTransaction       ***");
        String validTransactionHash = ethSendTx.getSendTransactionReult().getHash();
        testEthGetTransactionByHash(validTransactionHash);

        System.out.println("======================================");
        System.out.println("***  7.  eth_getTransactionCount   ***");
        Credentials credentials = Credentials.create(privateKey);        
        String validAccount = credentials.getAddress();
        DefaultBlockParameter param = DefaultBlockParameter.valueOf("latest");
        testEthGetTransactionCount(validAccount, param);

        System.out.println("======================================");
        System.out.println("***  8.  eth_getTransactionReceipt ***");
        String validContractAddress;
        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
            validTransactionHash).send();

        if(!ethGetTransactionReceipt.getTransactionReceipt().isPresent())
        {
            System.out.println("the result is null");
            return;
        } else {
            //is option_value is null return NoSuchElementException, else return option_value
            TransactionReceipt transactionReceipt =
                    ethGetTransactionReceipt.getTransactionReceipt().get();

            System.out.println("transactionHash:" + transactionReceipt.getTransactionHash());
            System.out.println("transactionIndex:" + transactionReceipt.getTransactionIndex());
            System.out.println("blockHash:" + transactionReceipt.getBlockHash());
            System.out.println("blockNumber:" + transactionReceipt.getBlockNumber());
            System.out.println("cumulativeGasUsed:" + transactionReceipt.getCumulativeGasUsed());
            System.out.println("gasUsed:" + transactionReceipt.getGasUsed());
            System.out.println("contractAddress:" + transactionReceipt.getContractAddress());
            validContractAddress = transactionReceipt.getContractAddress();
            System.out.println("logs.size:" + transactionReceipt.getLogs().size());
            System.out.println("root:" + transactionReceipt.getRoot());
            System.out.println("logsBloom:" + transactionReceipt.getLogsBloom());
            //System.out.println(transactionReceipt.getFrom());
            //System.out.println(transactionReceipt.getTo());
            //System.out.println(transactionReceipt.getLogs().get(0).getData());
        }

        System.out.println("======================================");
        System.out.println("***  9.  eth_getCode               ***");
        
        //DefaultBlockParameter parameter = DefaultBlockParameter.valueOf("latest");
        DefaultBlockParameter parameter = DefaultBlockParameter.valueOf("latest");
        testEthGetCode(validContractAddress, parameter);

        System.out.println("======================================");
        System.out.println("***  10. eth_call                  ***");
        String fromaddress = "1ed2f5d9eabfef6ab0a1b48237029310f538d05f";
        String contractAddress = validContractAddress;
        String encodedFunction = "6d4ce63c";
        DefaultBlockParameter paramete = DefaultBlockParameter.valueOf("latest");
        testEthCall(fromaddress, contractAddress, encodedFunction, paramete);
    }


    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    //1.  net_peerCount
    public void testNetPeerCount() throws Exception {
        NetPeerCount netPeerCount = web3j.netPeerCount().send();
        //support send asynchronous requests
        //NetPeerCount netPeerCount = web3j.netPeerCount().sendAsync().get();

        System.out.println("net_peerCount:" + netPeerCount.getQuantity());
    }

    //2.  cita_blockNumber
    public void testEthBlockNumber() throws Exception {
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();

        if(ethBlockNumber.isEmpty() == true){
            System.out.println("the result is null");
        } else {
            System.out.println("cita_blockNumber:" + ethBlockNumber.getBlockNumber());
        }
    }

    //3.  cita_getBlockByHash
    public void testEthGetBlockByHashReturnFullTransactionObjects(String validBlockHash, boolean isfullTran) throws Exception {
        //despite whether validBlockHash the prefix is 0x or not, the result is right
        //String validBlockHash = "0x6d25591072f28234dba1ee393b5611c32ed57bb6b857ef8c20bb0122eaf2d2df";

        EthBlock ethBlock = web3j.ethGetBlockByHash(validBlockHash, isfullTran).send();

        if(ethBlock.isEmpty() == true){
            System.out.println("the result is null");
        }
        else{
            EthBlock.Block block = ethBlock.getBlock();
            System.out.println("hash(blockhash):" + block.getHash());
            System.out.println("version:" + block.getVersion());
            //System.out.println("header.timestamp:" + block.getHeader().getTimeStamp());
            System.out.println("header.prevHash:" + block.getHeader().getPrevHash());
            System.out.println("header.number(hex):" + block.getHeader().getNumber());
            System.out.println("header.number(dec):" + block.getHeader().getNumberDec());
            System.out.println("header.stateRoot:" + block.getHeader().getStateRoot());
            System.out.println("header.transactionsRoot:" + block.getHeader().getTransactionsRoot());
            System.out.println("header.receiptsRoot:" + block.getHeader().getReceiptsRoot());
            System.out.println("header.gasUsed:" + block.getHeader().getGasUsed());
            System.out.println("header.proof.proposal:" + block.getHeader().getProof().getProposal());
            System.out.println("header.proof.height:" + block.getHeader().getProof().getHeight());
            System.out.println("header.proof.round:" + block.getHeader().getProof().getRound());

            if(!block.getBody().getTransactions().isEmpty()) {
                System.out.println("number of transaction:" + block.getBody().getTransactions().size());

                int i;
                for(i=0; i < block.getBody().getTransactions().size(); i++ ){
                    org.web3j.protocol.core.methods.response.Transaction tran = (org.web3j.protocol.core.methods.response.Transaction) block.getBody().getTransactions().get(i).get();
                    System.out.println("body.transactions.tranhash:" + tran.getHash());
                    System.out.println("body.transactions.content:" + tran.getContent());
                }
            }else{
                System.out.println("the block transactions is null");
            }
        }
    }

    //4.  cita_getBlockByNumber
    public void testEthGetBlockByNumberReturnTransactionObjects(BigInteger validBlockNumber, boolean isfullTranobj) throws Exception {
        //BigInteger validBlockNumber = BigInteger.valueOf(10);

        EthBlock ethBlock = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(validBlockNumber), isfullTranobj).send();

        if(ethBlock.isEmpty() == true){
            System.out.println("the result is null");
        }
        else{
            EthBlock.Block block = ethBlock.getBlock();
            System.out.println("hash(blockhash):" + block.getHash());
            System.out.println("version:" + block.getVersion());
            //System.out.println("header.timestamp:" + block.getHeader().getTimeStamp());
            System.out.println("header.prevHash:" + block.getHeader().getPrevHash());
            System.out.println("header.number(hex):" + block.getHeader().getNumber());
            System.out.println("header.number(dec):" + block.getHeader().getNumberDec());
            System.out.println("header.stateRoot:" + block.getHeader().getStateRoot());
            System.out.println("header.transactionsRoot:" + block.getHeader().getTransactionsRoot());
            System.out.println("header.receiptsRoot:" + block.getHeader().getReceiptsRoot());
            System.out.println("header.gasUsed:" + block.getHeader().getGasUsed());
            System.out.println("header.proof.proposal:" + block.getHeader().getProof().getProposal());
            System.out.println("header.proof.height:" + block.getHeader().getProof().getHeight());
            System.out.println("header.proof.round:" + block.getHeader().getProof().getRound());

            if(!block.getBody().getTransactions().isEmpty()) {
                System.out.println("number of transaction:" + block.getBody().getTransactions().size());

                int i;
                for(i=0; i < block.getBody().getTransactions().size(); i++ ){
                    org.web3j.protocol.core.methods.response.Transaction tran = (org.web3j.protocol.core.methods.response.Transaction) block.getBody().getTransactions().get(i).get();
                    System.out.println("body.transactions.tranhash:" + tran.getHash());
                    System.out.println("body.transactions.content:" + tran.getContent());
                }
            }else{
                System.out.println("the block transactions is null");
            }
        }
    }


    //5.  cita_sendTransaction
    public void testEthSendRawTransaction(String rawData ) throws Exception {
        //String rawData = "1ac9070a83071afa060a01301af4066060604052341561000f57600080fd5b5b6103558061001f6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635537f99e1461005f57806360fe47b1146100bc5780636d4ce63c146100df578063e2f5cc0d14610108575b600080fd5b341561006a57600080fd5b6100ba600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610197565b005b34156100c757600080fd5b6100dd60048080359060200190919050506101b2565b005b34156100ea57600080fd5b6100f26101bd565b6040518082815260200191505060405180910390f35b341561011357600080fd5b61011b6101c7565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561015c5780820151818401525b602081019050610140565b50505050905090810190601f1680156101895780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b80600190805190602001906101ad929190610270565b505b50565b806000819055505b50565b6000805490505b90565b6101cf6102f0565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102655780601f1061023a57610100808354040283529160200191610265565b820191906000526020600020905b81548152906001019060200180831161024857829003601f168201915b505050505090505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102b157805160ff19168380011785556102df565b828001600101855582156102df579182015b828111156102de5782518255916020019190600101906102c3565b5b5090506102ec9190610304565b5090565b602060405190810160405280600081525090565b61032691905b8082111561032257600081600090555060010161030a565b5090565b905600a165627a7a7230582059b58c613b38589c882547b0ac070977b9e3fc6629d05bc219ede6de1154705900292080808080101241e5885f60021af14c6fad1b01f459b54d7916e8ac0d46b34403cd7644766e7a997fe30bcb95dcd8607a86fc9f357aeb72dd56699ae1f288db3101c40bfaa7195d00208080808010";

        EthSendTransaction ethSendTx =  web3j.ethSendRawTransaction(rawData).send();

        if(ethSendTx.isEmpty() == true){
            System.out.println("the result is null");
        }
        else {
            System.out.println("hash(Transaction):" + ethSendTx.getSendTransactionReult().getHash());
            System.out.println("status:" + ethSendTx.getSendTransactionReult().getStatus());
        }
    }


    //6.  cita_getTransaction
    public void testEthGetTransactionByHash(String validTransactionHash) throws Exception {
        //despite whether validTransactionHash the prefix is 0x or not, the result is right
        //String validTransactionHash = "0x6681b04627546599baa4923d359fa89be9a81257376e1452b0ef5e0fbdfb3d2f";

        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(
                validTransactionHash).send();

        if(!ethTransaction.getTransaction().isPresent()){
            System.out.println("the result is null");
        } else {
            org.web3j.protocol.core.methods.response.Transaction transaction = ethTransaction.getTransaction().get();
            System.out.println("hash(Transaction):" + transaction.getHash());
            System.out.println("content:" + transaction.getContent());
            System.out.println("blockNumber(dec):" + transaction.getBlockNumber());
            System.out.println("blockHash:" + transaction.getBlockHash());
            System.out.println("index:" + transaction.getIndex());
        }
    }


    //7.  eth_getTransactionCount
    public void testEthGetTransactionCount(String validAccount, DefaultBlockParameter param) throws Exception {
        //despite whether validAccount the prefix is 0x or not, the result is right
        //String validAccount = "0xc63b1b7a3615fabf4caebe28878f07d4e23a9b8b";

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                validAccount, param).send();

        if(ethGetTransactionCount.isEmpty() == true){
            System.out.println("the result is null");
        } else {
            System.out.println("TransactionCount:" + ethGetTransactionCount.getTransactionCount());
        }

    }

    //8.  eth_getTransactionReceipt
    public void testEthGetTransactionReceipt(String validTransactionHash) throws Exception {
        //despite whether validTransactionHash the prefix is 0x or not, the result is right
        //String validTransactionHash = "0x6681b04627546599baa4923d359fa89be9a81257376e1452b0ef5e0fbdfb3d2f";
	
        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
                validTransactionHash).send();

        if(!ethGetTransactionReceipt.getTransactionReceipt().isPresent())
        {
            System.out.println("the result is null");
        } else {
            //is option_value is null return NoSuchElementException, else return option_value
            TransactionReceipt transactionReceipt =
                    ethGetTransactionReceipt.getTransactionReceipt().get();

            System.out.println("transactionHash:" + transactionReceipt.getTransactionHash());
            System.out.println("transactionIndex:" + transactionReceipt.getTransactionIndex());
            System.out.println("blockHash:" + transactionReceipt.getBlockHash());
            System.out.println("blockNumber:" + transactionReceipt.getBlockNumber());
            System.out.println("cumulativeGasUsed:" + transactionReceipt.getCumulativeGasUsed());
            System.out.println("gasUsed:" + transactionReceipt.getGasUsed());
            System.out.println("contractAddress:" + transactionReceipt.getContractAddress());
            System.out.println("logs.size:" + transactionReceipt.getLogs().size());
            System.out.println("root:" + transactionReceipt.getRoot());
            System.out.println("logsBloom:" + transactionReceipt.getLogsBloom());
            //System.out.println(transactionReceipt.getFrom());
            //System.out.println(transactionReceipt.getTo());
            //System.out.println(transactionReceipt.getLogs().get(0).getData());
        }

    }

    //9.  eth_getCode
    public void testEthGetCode(String validContractAddress, DefaultBlockParameter param) throws Exception {
        //despite whether validContractAddress the prefix is 0x or not, the result is right
	    //String validContractAddress = "0x50dfdb1833a4ba5c267f7d708d68d37b303eeb1d";

        EthGetCode ethGetCode = web3j.ethGetCode(validContractAddress, param).send();

        if(ethGetCode.isEmpty() == true){
            System.out.println("the result is null");
        } else {
            System.out.println("contractcode:" + ethGetCode.getCode());
        }
    }


    public Function totalSupply() {
        return new Function(
                "get",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {}));
    }

    //10.  eth_call
    public void testEthCall(String fromaddress, String contractAddress, String encodedFunction, DefaultBlockParameter param) throws Exception {
        //despite whether validContractAddress the prefix is 0x or not, the result is right
        //String fromaddress = "c63b1b7a3615fabf4caebe28878f07d4e23a9b8b";
        //String contractAddress = "50dfdb1833a4ba5c267f7d708d68d37b303eeb1d";
        //String encodedFunction = "6d4ce63c";

        /*
        Function function = totalSupply();
        String encodedFunction = FunctionEncoder.encode(function);
        System.out.println(encodedFunction);
        */

        EthCall ethCall = web3j.ethCall(
        new Call(fromaddress, contractAddress, encodedFunction),
                param).send();

	    System.out.println("call result:" + ethCall.getValue());
    }
}
