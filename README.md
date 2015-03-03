Java client for [Sherlok](http://sherlok.io/).

Here is an example that find person's names and places in text. It requires a Sherlok server running locally.

    # create client; alternatively, you can configure host and port
    SherlokClient client = new SherlokClient();

    # have Sherlok annotate some text with a text mining pipeline
    SherlokResult res = client.annotate("opennlp.ners.en", "Jack Burton "
    + "(born April 29, 1954 in El Paso), also known as Jake Burton, is an "
    + "American snowboarder and founder of Burton Snowboards.");

    # process the annotations (here: person's names and places)
    for (Annotation a : res.get("NamedEntity")) {
        System.out.println(a);
    }


