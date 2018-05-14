public class Tweet {
    String id;
    String text;
    // 1: positive, 0: notr, -1: negative
    int category;

    public Tweet (String id, String text, int category) {
        this.id = id;
        this.text = text;
        this.category = category;
    }
}
