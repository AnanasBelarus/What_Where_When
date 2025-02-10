package models;

import java.util.ArrayList;
import java.util.Collection;

public class Round extends ArrayList<AbstractQuestion> implements Runnable{
    private int order;
    private String title;

    public Round(int order, String title) {
        this.order = order;
        this.title = title;
    }

    public Round(Collection<? extends AbstractQuestion> c, int order, String title) {
        super(c);
        this.order = order;
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "Round [order=" + order + ", title=" + title +", size="+ size()+ "]";
    }

    @Override
    public void run() {
    }
}
