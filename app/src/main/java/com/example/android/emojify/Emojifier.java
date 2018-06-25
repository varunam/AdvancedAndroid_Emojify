/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class Emojifier {

    private static final String LOG_TAG = Emojifier.class.getSimpleName();
    private static final double EYE_OPEN_PROB_THRESHOLD = 0.7;
    private static final double SMILE_PROB_THRESHOLD = 0.3;
    private static boolean smiling = false, left_eye_closed = false, right_eye_closed = false;

    /**
     * Method for detecting faces in a bitmap.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static void detectFaces(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                // Log the classification probabilities for each face.
                whichEmoji(face);
                // TODO (6): Change the call to whichEmoji to whichEmoji() to log the appropriate emoji for the facial expression.
            }

        }


        // Release the detector
        detector.release();
    }


    /**
     * Method for logging the classification probabilities.
     *
     * @param face The face to get the classification probabilities.
     */
    private static Emoji whichEmoji(Face face){
        // TODO (2): Change the name of the whichEmoji() method to whichEmoji() (also change the log statements)
        // Log all the probabilities
        Log.d(LOG_TAG, "whichEmoji: smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());

        // TODO (3): Create threshold constants for a person smiling, and and eye being open by taking pictures of yourself and your friends and noting the logs.

        // TODO (4): Create 3 boolean variables to track the state of the facial expression based on the thresholds you set in the previous step: smiling, left eye closed, right eye closed.
        // TODO (5): Create an if/else system that selects the appropriate emoji based on the above booleans and log the result.
        if(face.getIsSmilingProbability()>SMILE_PROB_THRESHOLD)
            smiling = true;
        if(face.getIsLeftEyeOpenProbability()<EYE_OPEN_PROB_THRESHOLD)
            left_eye_closed = true;
        if(face.getIsRightEyeOpenProbability()<EYE_OPEN_PROB_THRESHOLD)
            right_eye_closed = true;

        Emoji emoji;
        if(smiling)
        {
            if(left_eye_closed && !right_eye_closed)
                emoji = Emoji.left_wink;
            else if(!left_eye_closed && right_eye_closed)
                emoji = Emoji.right_wink;
            else if(left_eye_closed)
                emoji = Emoji.closed_eye_smiling;
            else
                emoji = Emoji.smiling;
        }
        else
        {
            if(left_eye_closed && !right_eye_closed)
                emoji = Emoji.left_wink_frown;
            else if(!left_eye_closed && right_eye_closed)
                emoji = Emoji.right_wink_frown;
            else if(left_eye_closed)
                emoji = Emoji.closed_eye_frowning;
            else
                emoji = Emoji.frowning;
        }

        Log.e(LOG_TAG,"Emoji: " + emoji.name());
        smiling = left_eye_closed = right_eye_closed = false;
        return emoji;
    }


    // TODO (1): Create an enum class called Emoji that contains all the possible emoji you can make (smiling, frowning, left wink, right wink, left wink frowning, right wink frowning, closed eye smiling, close eye frowning).
    enum Emoji{
        smiling,
        frowning,
        left_wink,
        right_wink,
        left_wink_frown,
        right_wink_frown,
        closed_eye_frowning,
        closed_eye_smiling;
    }
}
