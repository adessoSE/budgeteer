package org.wickedsource.budgeteer.web.components.customFeedback;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * A feedbackPanel according to the struktur and style-classes of AdminLTE
 */
public class CustomFeedbackPanel extends Panel implements IFeedback {

    private final class MessageListView extends ListView<FeedbackMessage> {
        private static final long serialVersionUID = 1L;

        public MessageListView(final String id) {
            super(id);
            setDefaultModel(newFeedbackMessagesModel());
        }

        @Override
        protected void populateItem(final ListItem<FeedbackMessage> listItem) {
            final FeedbackMessage message = listItem.getModelObject();
            message.markRendered();
            WebMarkupContainer messageContainer = new WebMarkupContainer("messageContainer");
            messageContainer.add(new AttributeAppender("class",new Model<String>(getFeedbackContainerClass(message.getLevel())), " "));

            Label messageIcon = new Label("messageIcon","");
            messageIcon.add(new AttributeAppender("class", new Model<String>(getFeedbackIconClass(message.getLevel()))," "));
            messageContainer.add(messageIcon);

            MultiLineLabel text = new MultiLineLabel("messageText", message.getMessage().toString());
            messageContainer.add(text);

            listItem.add(messageContainer);
        }

        /**
         * Returns the right style-class for the Feedbackmassage-Icon according to the Level or the message
         * @return one of the classes "fa-warning", "fa-ban", "fa-info", "fa-check"
         */
        private String getFeedbackIconClass(int level){
            String result = "fa-warning";
            switch(level){
                case FeedbackMessage.FATAL:
                    result = "fa-ban";
                    break;
                case FeedbackMessage.ERROR:
                    result = "fa-ban";
                    break;
                case FeedbackMessage.INFO:
                    result = "fa-info";
                    break;
                case FeedbackMessage.SUCCESS:
                    result = "fa-check";
                    break;
            }
            return result;
        }

        /**
         * Returns the right style-class for the Feedbackmassage according to the Level or the message
         * @return one of the classes "alert-warning", "alert-info", "alert-danger", "alert-danger"
         */
        private String getFeedbackContainerClass(int level){
            String result = "alert-warning";
            switch(level){
                case FeedbackMessage.FATAL:
                    result = "alert-danger";
                    break;
                case FeedbackMessage.ERROR:
                    result = "alert-danger";
                    break;
                case FeedbackMessage.INFO:
                    result = "alert-info";
                    break;
                case FeedbackMessage.SUCCESS:
                    result = "alert-success";
                    break;
            }
            return result;
        }
    }

    private static final long serialVersionUID = 1L;


    public CustomFeedbackPanel(final String id) {
        super(id);
        final MessageListView messageListView = new MessageListView("messages");
        WebMarkupContainer messagesContainer = new WebMarkupContainer("feedbackContainer") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(!messageListView.getModelObject().isEmpty());
            }
        };
        add(messagesContainer);
        messageListView.setVersioned(false);
        messagesContainer.add(messageListView);
    }


    /**
     * @see org.apache.wicket.Component#isVersioned()
     */
    @Override
    public boolean isVersioned() {
        return false;
    }


    /**
     * Gets a new instance of FeedbackMessagesModel to use.
     *
     * @return Instance of FeedbackMessagesModel to use
     */
    protected FeedbackMessagesModel newFeedbackMessagesModel() {
        return new FeedbackMessagesModel(this);
    }

}