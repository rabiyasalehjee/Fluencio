package com.mobitech.speachtotext.textToSpeech.model;

public class TopicModel {
    //"ID": 3,
    //      "topicHintfileRoute": "/getTopicFiles?filename=topicHint_3_img.PNG",
    //      "topicfileRoute":
    String topic_id,topichint_img_url,topic_img_url;

    public TopicModel() {
    }

    public TopicModel(String topic_id, String topichint_img_url, String topic_img_url) {
        this.topic_id = topic_id;
        this.topichint_img_url = topichint_img_url;
        this.topic_img_url = topic_img_url;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopichint_img_url() {
        return topichint_img_url;
    }

    public void setTopichint_img_url(String topichint_img_url) {
        this.topichint_img_url = topichint_img_url;
    }

    public String getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(String topic_img_url) {
        this.topic_img_url = topic_img_url;
    }
}
