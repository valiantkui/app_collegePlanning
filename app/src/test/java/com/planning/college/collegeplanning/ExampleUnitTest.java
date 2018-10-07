package com.planning.college.collegeplanning;

import android.util.Log;

import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);



        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/subject/test").build();
        Call call = okHttpClient.newCall(request);
        final String[] jsonStr = new String[1];


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                jsonStr[0] =  response.body().string();

                Log.i("tag", jsonStr[0]);
            }
        });

    }
}