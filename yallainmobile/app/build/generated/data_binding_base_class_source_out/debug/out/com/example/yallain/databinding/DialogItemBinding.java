// Generated by view binder compiler. Do not edit!
package com.example.yallain.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.yallain.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button buttonCancel;

  @NonNull
  public final Button buttonRent;

  @NonNull
  public final EditText editTextHours;

  @NonNull
  public final TextView textViewMessage;

  @NonNull
  public final TextView textViewTitle;

  private DialogItemBinding(@NonNull LinearLayout rootView, @NonNull Button buttonCancel,
      @NonNull Button buttonRent, @NonNull EditText editTextHours,
      @NonNull TextView textViewMessage, @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.buttonCancel = buttonCancel;
    this.buttonRent = buttonRent;
    this.editTextHours = editTextHours;
    this.textViewMessage = textViewMessage;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_cancel;
      Button buttonCancel = ViewBindings.findChildViewById(rootView, id);
      if (buttonCancel == null) {
        break missingId;
      }

      id = R.id.button_rent;
      Button buttonRent = ViewBindings.findChildViewById(rootView, id);
      if (buttonRent == null) {
        break missingId;
      }

      id = R.id.edit_text_hours;
      EditText editTextHours = ViewBindings.findChildViewById(rootView, id);
      if (editTextHours == null) {
        break missingId;
      }

      id = R.id.text_view_message;
      TextView textViewMessage = ViewBindings.findChildViewById(rootView, id);
      if (textViewMessage == null) {
        break missingId;
      }

      id = R.id.text_view_title;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new DialogItemBinding((LinearLayout) rootView, buttonCancel, buttonRent, editTextHours,
          textViewMessage, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
