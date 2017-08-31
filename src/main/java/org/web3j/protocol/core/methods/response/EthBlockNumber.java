package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * eth_blockNumber.
 */
public class EthBlockNumber extends Response<String> {

    public boolean isEmpty() {
        if(getResult() == null)
            return true;
        else
            return false;
    }

    public BigInteger getBlockNumber() {
        return Numeric.decodeQuantity(getResult());
    }
}
