package backend.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class TimestampMessageDecoder implements Decoder.Text<TimestampMessage>  {
    @Override
    public TimestampMessage decode(String s) throws DecodeException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TimestampMessage m = objectMapper.readValue(s, TimestampMessage.class);
            return m;
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, "Could not encode.", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TimestampMessage m = objectMapper.readValue(s, TimestampMessage.class);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
