// Generated code from Butter Knife. Do not modify!
package com.foxes.capstone;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ParentalSettingsFragment_ViewBinding<T extends ParentalSettingsFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ParentalSettingsFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.button_edit_users = Utils.findRequiredViewAsType(source, R.id.button_edit_users, "field 'button_edit_users'", Button.class);
    target.button_edit_applist = Utils.findRequiredViewAsType(source, R.id.button_edit_applist, "field 'button_edit_applist'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.button_edit_users = null;
    target.button_edit_applist = null;

    this.target = null;
  }
}
