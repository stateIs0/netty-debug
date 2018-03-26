package io.netty.example.echo;

import io.netty.util.Recycler;
import io.netty.util.Recycler.Handle;

public class RecyclerDemo {

  static final Recycler recycle = new Recycler() {
    @Override
    protected Object newObject(Handle handle) {

      return new RecyclerDemo(handle);
    }
  };

  public RecyclerDemo(Handle handle) {
  }


  public static void main(String[] args) {
    RecyclerDemo demo = (RecyclerDemo) recycle.get();
    demo.recycle();

  }

  Handle handle;

  private void recycle() {
    handle.recycle(this);
  }
}
