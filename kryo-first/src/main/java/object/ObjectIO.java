package object;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryo.io.KryoObjectOutput;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectIO {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    doWithFile();

    doByList();
  }

  static void doByList() throws IOException, ClassNotFoundException {
    String fileName = "books_list.data";

    Kryo kryo = new Kryo();
    kryo.register(Book.class);
    kryo.register(Arrays.asList().getClass());

    {
      // write two record
      FileOutputStream fos = new FileOutputStream(fileName);
      Output output = new Output(fos);

      KryoObjectOutput objectOutput = new KryoObjectOutput(kryo, output);
      objectOutput.writeObject(Arrays.asList(
              new Book(1, "one"),
              new Book(2, "two"),
              new Book(3, "three")
      ));
      objectOutput.close();

      FileInputStream fis = new FileInputStream(fileName);
      Input input = new Input(fis);
      KryoObjectInput objectInput = new KryoObjectInput(kryo, input);
      List<Book> books = (List<Book>)objectInput.readObject();
      for(Book book : books)
        System.out.println(book);
    }
  }

  static void doWithFile() throws IOException, ClassNotFoundException {

    String fileName = "books.data";

    Kryo kryo = new Kryo();
    kryo.register(Book.class);

    {
      // write two record
      FileOutputStream fos = new FileOutputStream(fileName);
      Output output = new Output(fos);

      KryoObjectOutput objectOutput = new KryoObjectOutput(kryo, output);
      objectOutput.writeObject(new Book(1, "one"));
      objectOutput.writeObject(new Book(2, "two"));
      objectOutput.close();
    }

    {
      // append one record
      FileOutputStream fos = new FileOutputStream(fileName, true);
      Output output = new Output(fos);

      KryoObjectOutput objectOutput = new KryoObjectOutput(kryo, output);
      objectOutput.writeObject(new Book(3, "three"));
      objectOutput.close();
    }

    {
      // read all
      long size = Files.size(Paths.get(fileName));

      FileInputStream fis = new FileInputStream(fileName);
      Input input = new Input(fis);
      KryoObjectInput objectInput = new KryoObjectInput(kryo, input);

      while (input.position() < size) {
        Book book = (Book) objectInput.readObject();
        System.out.println(book);
      }
    }

  }
}