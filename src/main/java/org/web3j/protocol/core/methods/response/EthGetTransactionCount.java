package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * eth_getTransactionCount.
 */
public class EthGetTransactionCount extends Response<String> {

    public boolean isEmpty() {
        if(getResult() == null)
            return true;
        else
            return false;
    }

    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
