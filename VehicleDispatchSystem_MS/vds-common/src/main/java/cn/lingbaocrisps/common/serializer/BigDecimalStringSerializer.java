package cn.lingbaocrisps.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalStringSerializer extends StdSerializer<BigDecimal> {

    public final static BigDecimalStringSerializer instance = new BigDecimalStringSerializer();

    public BigDecimalStringSerializer() {
        super(BigDecimal.class);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeString("0");
        } else {
            String val = value.stripTrailingZeros().toPlainString();
            gen.writeString(val);
        }
    }


}
