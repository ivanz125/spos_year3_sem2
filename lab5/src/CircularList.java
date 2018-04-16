public class CircularList {

    private CircularListNode head;
    private CircularListNode hand;

    public void insert(Page page) {
        if (hand == null) {
            CircularListNode node = new CircularListNode(page, null);
            node.next = node;
            head = node;
            hand = node;
        }
        else {
            CircularListNode node = new CircularListNode(page, head);
            hand.next = node;
            hand = node;
        }
    }

    public void modeHandToStart() {
        hand = head;
    }

    public Page currentPage() {
        return hand == null ? null : hand.page;
    }

    public Page moveHand() {
        if (hand == null) return null;
        hand = hand.next;
        return hand.page;
    }

    public void replaceAtHand(Page newPage) {
        hand.page = newPage;
    }
}
