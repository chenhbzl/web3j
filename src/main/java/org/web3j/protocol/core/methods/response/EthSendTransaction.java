package org.web3j.protocol.core.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

import java.io.IOException;

/**
 * eth_sendTransaction.
 */
public class EthSendTransaction extends Response<EthSendTransaction.SendTransactionReult> {

    public SendTransactionReult getSendTransactionReult() {
        return getResult();
    }

    public boolean isEmpty() {
        if(getResult() == null)
            return true;
        else
            return false;
    }

    public static class SendTransactionReult {
        private String hash;
        private String status;

        public SendTransactionReult() {
        }

        public SendTransactionReult(String hash, String status) {
            this.hash = hash;
            this.status = status;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) { this.hash = hash; }

        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }
    }

/*
    public static class ResponseDeserialiser extends JsonDeserializer<SendTransactionReult> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public SendTransactionReult deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, SendTransactionReult.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
*/
}
