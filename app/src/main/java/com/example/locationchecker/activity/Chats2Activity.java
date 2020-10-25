package com.example.locationchecker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationchecker.R;
import com.example.locationchecker.adapter.MessagesAdapter;
import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.Messages;
import com.example.locationchecker.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chats2Activity extends AppCompatActivity {
    private String messageReceiverID, userName, userImage, messageSenderID;
    private TextView chatName, chatLastSeen, chatSent;
    private FirebaseAuth auth;
    private CircleImageView ciChats;
    private EditText edtMessage;
    private ImageView imgMessage, imgFile, imgMedia, imgCallVdieo;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;
    private final List<Messages> arrMessage = new ArrayList<>();
    private MessagesAdapter messagesAdapter;
    private RecyclerView rvChatsLayout;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private String saveCurrentTime, saveCurrentDate;
    private String checker = "", myURL = "";
    private StorageTask uploadTask;
    private DatabaseReference referenceSeen;
    private Uri fileUri;
    private ValueEventListener seenLisener;
    private LocationManager locationManager;
    private String myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats2);

        auth = FirebaseAuth.getInstance();
        //sua cho nay

//        messageSenderID = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        chatName = findViewById(R.id.tv_name_chat_kid);
        chatLastSeen = findViewById(R.id.tv_last_seen);
        ciChats = findViewById(R.id.chat_img);
        edtMessage = findViewById(R.id.edt_message_kid);
        imgMessage = findViewById(R.id.img_send_kid);
        rvChatsLayout = findViewById(R.id.rv_chat_layout_kid);
//        imgCallVdieo = findViewById(R.id.btn_video_call);
//        imgFile = findViewById(R.id.img_file);
//        imgMedia = findViewById(R.id.btn_media);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        progressDialog = new ProgressDialog(this);


//        imgMedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChatsActivity.this, MediaActivity.class);
//                intent.putExtra("visit_sender_id", messageSenderID);
//                intent.putExtra("visit_receiver_id", messageReceiverID);
//                startActivity(intent);
//            }
//        });
//        ciChats.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChatsActivity.this, ProfileFriend.class);
//                intent.putExtra("visit_user_id", messageReceiverID);
//                startActivity(intent);
//            }
//        });

        messageReceiverID = getIntent().getExtras().get("userid").toString();
        reference.child("Users").child(messageReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Parent user = snapshot.getValue(Parent.class);
                chatName.setText(user.getName());
                messageSenderID = user.getCode();
                Picasso.get().load(R.drawable.profile_image).placeholder(R.drawable.profile_image).into(ciChats);
//                if (snapshot.hasChild("imageURL")) {
//                    String receiverImage = snapshot.child("imageURL").getValue().toString();
//                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(ciChats);
//                }

//                retrieveMessage(messageSenderID,messageReceiverID, messageReceiverID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        imgMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessager();
            }
        });
        retrieveMessage(messageSenderID,messageReceiverID);



    }
    private void sendMessager() {
        String messageText = edtMessage.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "tin nhắn trống", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference ref  = FirebaseDatabase.getInstance().getReference();
            String messageKey = reference.push().getKey();
            DatabaseReference refMes = ref.child("Message").child(messageReceiverID).child(messageKey);
            HashMap<String, Object> mes = new HashMap<>();
            mes.put("sender", messageSenderID);
            mes.put("receiver", messageReceiverID);
            mes.put("message",messageText);
            refMes.updateChildren(mes);
            edtMessage.setText("");

        }
    }

    private void retrieveMessage(final String senderId, final String receiverId) {
        reference = FirebaseDatabase.getInstance().getReference().child("Message").child(messageReceiverID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrMessage.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Messages messages = ds.getValue(Messages.class);
                    if (messages.getSender() == senderId && messages.getReceiver() == receiverId ||
                            messages.getReceiver() == senderId && messages.getSender() == receiverId
                    )
                    {
                        arrMessage.add(messages);
                        Log.d("MSG",messages.getMessage());
                    }
                    messagesAdapter = new MessagesAdapter(
                            arrMessage,
                            getApplicationContext()
                    );
                    rvChatsLayout.setAdapter(messagesAdapter);
                    rvChatsLayout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void displayLastSeen() {
//        reference.child("Users").child(messageReceiverID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("userState").hasChild("state")) {
//                    String state = snapshot.child("userState").child("state").getValue().toString();
//                    String date = snapshot.child("userState").child("date").getValue().toString();
//                    String time = snapshot.child("userState").child("time").getValue().toString();
//                    if (state.equals("online")) {
//                        chatLastSeen.setText("online");
//                    } else if (state.equals("offline")) {
//                        chatLastSeen.setText("Hoạt động :" + date + " " + time);
//                    }
//                } else {
//
//                    chatLastSeen.setText("offline");
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}