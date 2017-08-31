package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * eth_getCode.
 */
public class EthGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }

    public boolean isEmpty() {
        if(getResult() == null)
            return true;
        else
            return false;
    }
}
