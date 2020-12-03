package com.example.a20qprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Messages, MessageViewHolder> firebaseAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ImageView addMessageImageView;
    public  String partner;
    public  String MESSAGES_CHILD;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        partner = getIntent().getStringExtra("prtnr");
        mAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mAuth.getCurrentUser();
        MESSAGES_CHILD = "Messages/"+mFirebaseUser.getDisplayName()+"_"+partner;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        // UNCOMMENT TO UPDATE FIREBASE.UI //
        SnapshotParser<Messages> parser = new SnapshotParser<Messages>() {
            @Override
            public Messages parseSnapshot(DataSnapshot dataSnapshot) {
                Messages message = dataSnapshot.getValue(Messages.class);
                if (message != null) {
                    message.setMessageId(dataSnapshot.getKey());
                }
                return message;
            }
        };
        final DatabaseReference messageRef =databaseReference.child(MESSAGES_CHILD);
        Query firebaseSearchQuery = messageRef.orderByChild("timestamp");

        firebaseAdapter = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>( parser,R.layout.chat_item_self,MessageViewHolder.class,firebaseSearchQuery){
            @Override
            protected void populateViewHolder(final MessageViewHolder messageViewHolder, Messages model, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (model.getMessage() != null) {
                    messageViewHolder.messageTextView.setText(model.getMessage());
                    messageViewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    //messageViewHolder.messageImageView.setVisibility(ImageView.GONE);
                }
                // UNCOMMENT TO IMPLEMENT IMAGES //
                /*else if (model.getImageUrl() != null) {
                    String imageUrl = model.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            Glide.with(messageViewHolder.messageImageView.getContext())
                                                    .load(downloadUrl)
                                                    .into(messageViewHolder.messageImageView);
                                        } else {
                                            Log.w("TAG", "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                    }
                    else {
                        Glide.with(messageViewHolder.messageImageView.getContext())
                                .load(model.getImageUrl())
                                .into(messageViewHolder.messageImageView);
                    }
                    messageViewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    messageViewHolder.messageTextView.setVisibility(TextView.GONE);
                }*/

                /*if (model.getPhotoUrl() == null) {
                    messageViewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(ConversationActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(ConversationActivity.this)
                            .load(model.getPhotoUrl())
                            .into(messageViewHolder.messengerImageView);
                }*/

            }
        };
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messagesCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messagesCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    messageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        messageRecyclerView.setAdapter(firebaseAdapter);

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messages message = new
                        Messages(mFirebaseUser.getDisplayName()+"_"+partner,
                        null,
                        null,
                        messageEditText.getText().toString(),
                        null,
                        "TEXT",
                        mFirebaseUser.getDisplayName(),
                        "000"

                );
                databaseReference.child(MESSAGES_CHILD)
                        .push().setValue(message);
                messageEditText.setText("");
            }
        });
        /*addMessageImageView = (ImageView)findViewById(R.id.addMessageImageView);
        addMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static class MessageViewHolder extends  RecyclerView.ViewHolder{
        TextView messageTextView;
        /*ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;*/

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            /*messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);*/
        }
    }

    public static class Messages {

        public Messages(String chatId, String mediaThumbUrl, String mediaUrl, String message, String messageId, String messageType, String senderId, String timestamp) {
            this.chatId = chatId;
            this.mediaThumbUrl = mediaThumbUrl;
            this.mediaUrl = mediaUrl;
            this.message = message;
            this.messageId = messageId;
            this.messageType = messageType;
            this.senderId = senderId;
            this.timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        }

        public String getChatId() {
            return chatId;
        }

        public String getMediaThumbUrl() {
            return mediaThumbUrl;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public String getMessage() {
            return message;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getMessageType() {
            return messageType;
        }

        public String getSenderId() {
            return senderId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

        public void setMediaThumbUrl(String mediaThumbUrl) {
            this.mediaThumbUrl = mediaThumbUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        private String chatId, mediaThumbUrl, mediaUrl, message, messageId, messageType, senderId,timestamp;

        public Messages(){ }

    }


}