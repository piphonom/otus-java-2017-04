import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.lesson8.JSONObjectWriter;
import ru.otus.lesson8.bags.SecondBagOfObjects;

/**
 * Created by piphonom
 */
public class JSONObjectWriterTest {
    @Test
    public void primitivesTest() {
        SecondBagOfObjects bag = new SecondBagOfObjects();
        String json = JSONObjectWriter.toJson(bag);
        Gson gson = new Gson();
        String gjson = gson.toJson(bag);
        Assert.assertEquals(gjson, json);
    }
}
