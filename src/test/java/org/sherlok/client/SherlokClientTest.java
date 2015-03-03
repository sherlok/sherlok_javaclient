package org.sherlok.client;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;
import org.sherlok.mappings.Annotation;
import org.sherlok.mappings.SherlokResult;

/**
 * Hum: cannot test this Sherlok client with Sherlok server, because of Maven
 * circular dependencies. However, this Sherlok client code is thouroughly
 * tested in Sherlok server (sherlok_core).
 */
@Ignore
public class SherlokClientTest {

    final String DEFAULT_PIPELINE = "opennlp.ners.en";
    final String TEST_TEXT = "Jack Burton (born April 29, 1954 in El Paso), also known as Jake Burton, is an American snowboarder and founder of Burton Snowboards.";

    @Test
    public void test() throws Exception {

        SherlokClient client = new SherlokClient();

        SherlokResult res = client.annotate(DEFAULT_PIPELINE, TEST_TEXT);

        for (Entry<Integer, Annotation> a : res.getAnnotations().entrySet()) {
            System.out.println(a.getValue());
        }

        List<Annotation> entities = res.get("NamedEntity");
        assertEquals(3, entities.size());
    }
}
