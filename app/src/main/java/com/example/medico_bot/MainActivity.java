package com.example.medico_bot;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<ResponseMessage> responseMessageList;
    TextToSpeech t1;
    public boolean turn1=true,turn2=false,complete=true,just_finished=false,chat_begins=true;

    public int check =1,common=0;



    // tflite graph



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput=findViewById(R.id.userInput);
        recyclerView=findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });









        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {




            int t=0;



            String sym_list ="";
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {

                    ResponseMessage responseMessage = new ResponseMessage(userInput.getText().toString(), true);
                    responseMessageList.add(responseMessage);



                    ResponseMessage responseMessage1 = new ResponseMessage("dummy",false);

                    if(check==0 && complete==false) {
                        String resp = userInput.getText().toString();
                        String sym;


                        if (turn1) {


                            if (!(resp.contains("no") || resp.contains("nothing") || resp.contains("thats it") || resp.contains("thats all") || resp.contains("done"))) {
                                if (t % 4 == 0) {
                                    responseMessage1 = new ResponseMessage("What is the symptom?", false);
                                    responseMessageList.add(responseMessage1);


                                    String str = "What is the symptom?";
                                    String toSpeak = str.toLowerCase();
                                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                }

                                if (t % 4 == 1) {
                                    responseMessage1 = new ResponseMessage("Symptom?", false);
                                    responseMessageList.add(responseMessage1);

                                    String str = "Symptom?";
                                    String toSpeak = str.toLowerCase();
                                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                }

                                if (t % 4 == 2) {
                                    responseMessage1 = new ResponseMessage("May i know the symptom?", false);
                                    responseMessageList.add(responseMessage1);

                                    String str = "May i know the symptom?";
                                    String toSpeak = str.toLowerCase();
                                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                }

                                if (t % 4 == 3) {
                                    responseMessage1 = new ResponseMessage("Ohh okay what was the symptom?", false);
                                    responseMessageList.add(responseMessage1);

                                    String str = "Ohh okay what was the symptom?";
                                    String toSpeak = str.toLowerCase();
                                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                }

                                t++;
                            }

                            else {

                                check = 1;
                                complete=true;
                                just_finished=true;








                            }

                            turn2=true;
                            turn1=false;




                        }


                        else
                        {
                            sym=userInput.getText().toString();

                            if(sym.contains("cough") || sym.contains("sneeze") || sym.contains("fever"))
                            {
                                common=1;
                                responseMessage1 = new ResponseMessage("These are more common symptoms could you be more specific. Do u have any other symptoms other than these?", false);
                                responseMessageList.add(responseMessage1);

                                String str = "These are more common symptoms could you be more specific. Do u have any other symptoms other than these?";
                                String toSpeak = str.toLowerCase();


                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


                            }

                            sym_list=sym_list.concat(" "+sym);





                            if(common==0) {


                                responseMessage1 = new ResponseMessage("Any additional symptom faced?", false);
                                responseMessageList.add(responseMessage1);

                                String str = "Any additional symptom faced?";
                                String toSpeak = str.toLowerCase();


                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                            }

                            else
                                common=0;


                            turn1=true;
                            turn2=false;




                        }


                    }






                     TensorFlowInferenceInterface tensorFlowInferenceInterface;


                    tensorFlowInferenceInterface = new  TensorFlowInferenceInterface(getAssets(),"chat_model.pb");




                    if(check!=0 && complete) {
                        float[] res = {0, 0, 0, 0, 0,0};

                        float[] bag = new float[53];
                        Random rand = new Random();

                        // String[] labels = {"goodbye", "greeting", "headache", "opentoday", "payments", "thanks"};

                        String[] words = new String[]{",", "aches", "age", "alright", "anyone", "body", "bye", "call", "called",
                                "cold", "congestion", "cough", "diagnosis", "drip", "dude", "eyes", "fever", "feverish", "flu",
                                "good", "goodbye", "headache", "headaches", "hello", "help", "hey", "itchy", "later", "morning",
                                "name", "nasal", "need", "nice", "nose", "old", "pleasure", "postnasal", "problem", "runny",
                                "scratchy", "see", "sneezing", "soon", "sore", "stuffy", "suggestion", "talking", "there", "throat",
                                "up", "wassup", "watery", "whats"};

                        int j;
                        for (j = 0; j < words.length; j++) {

                            StringTokenizer str1;

                            if(!just_finished)
                                 str1 = new StringTokenizer(responseMessage.getText(), " ");
                            else
                                str1 = new StringTokenizer(sym_list, " ");


                            while (str1.hasMoreTokens()) {


                                String str = new String(str1.nextToken());


                                if (str.equals(words[j]))

                                    bag[j] = (float) 1.0;
                            }
                        }

                        tensorFlowInferenceInterface.feed("my_input/X", bag, 1, words.length);
                        tensorFlowInferenceInterface.run(new String[]{"my_outpu/Softmax"});
                        tensorFlowInferenceInterface.fetch("my_outpu/Softmax", res);

                        float max = res[0];
                        int index = 0;
                        for (j = 0; j < 6; j++) {
                            if (res[j] > max) {
                                max = res[j];
                                index = j;
                            }
                        }


                        if (max < 0.8) {

                            if(just_finished) {

                                responseMessage1 = new ResponseMessage("I cannot predict with your inputs please try to provide more information  ", false);
                                responseMessageList.add(responseMessage1);

                                String str = "I cannot predict with your inputs please try to provide more information";
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                            }

                            else
                            {
                                responseMessage1 = new ResponseMessage("Cannot understand u ", false);
                                responseMessageList.add(responseMessage1);

                                String str = "cannot understand you";
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else {


                            if (index == 0) {

                                int rand_int1 = rand.nextInt(4);

                                ArrayList<String> responses = new ArrayList<String>();

                                responses.add("Hi i am 2 months old");
                                responses.add("Since Nov 2019");
                                responses.add("My age is 2 months ");
                                responses.add("Its been 2month since my entry to this world");


                                responseMessage1 = new ResponseMessage(responses.get(rand_int1), false);
                                responseMessageList.add(responseMessage1);

                                String str = responses.get(rand_int1);
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


                            }

                            if (index == 2) {
                                int rand_int1 = rand.nextInt(5);

                                ArrayList<String> responses = new ArrayList<String>();

                                responses.add("Okay see u later");
                                responses.add("Good bye!!!!");
                                responses.add("Will miss u ");
                                responses.add("Always there for u!");
                                responses.add("Have a nice day");


                                responseMessage1 = new ResponseMessage(responses.get(rand_int1), false);
                                responseMessageList.add(responseMessage1);

                                String str = responses.get(rand_int1);
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                            if (index == 3) {
                                int rand_int1 = rand.nextInt(4);

                                ArrayList<String> responses = new ArrayList<String>();

                                responses.add("Good to see u again");
                                responses.add("Welcome dude");
                                responses.add("Happy to see u ");
                                responses.add("Whats the problem");


                                responseMessage1 = new ResponseMessage(responses.get(rand_int1), false);
                                responseMessageList.add(responseMessage1);

                                String str = responses.get(rand_int1);
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                            if (index == 5) {
                                int rand_int1 = rand.nextInt(4);

                                ArrayList<String> responses = new ArrayList<String>();

                                responses.add("My name is Medicobot");
                                responses.add("Meicobot is my name");
                                responses.add("Developer call me Medicobot!!");
                                responses.add("Call me Medicobot!");


                                responseMessage1 = new ResponseMessage(responses.get(rand_int1), false);
                                responseMessageList.add(responseMessage1);

                                String str = responses.get(rand_int1);
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }

                            if (index == 4) {
                                int rand_int1 = rand.nextInt(3);

                                ArrayList<String> responses = new ArrayList<String>();

                                responses.add("Ohh so sorry about that have u faced any symptoms(YES/NO)");
                                responses.add("Ohh okay did u experience any symptom(YES/NO");

                                responses.add("Sorry to hear it.Can u say me whether u faced a symptom(YES/NO)");


                                responseMessage1 = new ResponseMessage(responses.get(rand_int1), false);
                                responseMessageList.add(responseMessage1);


                                String str = responses.get(rand_int1);
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                check = 0;
                                complete=false;


                            }

                            if (index == 1) {
                                responseMessage1 = new ResponseMessage("As per your feed i predict that u may be suffering from allergy or cold", false);
                                responseMessageList.add(responseMessage1);


                                String str = "As per your feed i predict that u may be suffering from allergy or cold";
                                String toSpeak = str.toLowerCase();
                                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);




                            }


                        }

                    }




                    if(check==1 && complete)
                        just_finished=false;

                    messageAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }
                return false;
            }
        });
    }
    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }



}
