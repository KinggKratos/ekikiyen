/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blackground.ekikiyen.ocr;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.util.regex.Pattern;

import blackground.ekikiyen.camera.GraphicOverlay;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 * TODO: Make this implement Detector.Processor<TextBlock> and add text to the GraphicOverlay
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private OnCardFound onCardFound;

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay,
                                OnCardFound onCardFound) {
        mGraphicOverlay = ocrGraphicOverlay;
        this.onCardFound = onCardFound;
    }

    // TODO:  Once this implements Detector.Processor<TextBlock>, implement the abstract methods.
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected! " + item.getValue());
            }
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);

            String textBlock = graphic.getTextBlock().getValue();
            // remove all spaces and check if they contain only numbers
            textBlock = textBlock.replaceAll(" ", "");
            if (Pattern.matches("\\d{13,}", textBlock)) {
                mGraphicOverlay.add(graphic);
                onCardFound.found(textBlock);
            }
        }
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    public interface OnCardFound {
        void found(String value);
    }
}