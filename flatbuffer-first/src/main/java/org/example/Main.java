package org.example;

import com.google.flatbuffers.FlatBufferBuilder;
import mygame.sample.*;

public class Main {

  static byte[] createMonster() {
    FlatBufferBuilder builder = new FlatBufferBuilder(1024);

    int weaponOneName = builder.createString("Sword");
    short weaponOneDamage = 3;

    int weaponTwoName = builder.createString("Axe");
    short weaponTwoDamage = 5;

    // Use the `createWeapon()` helper function to create the weapons, since we set every field.
    int sword = Weapon.createWeapon(builder, weaponOneName, weaponOneDamage);
    int axe = Weapon.createWeapon(builder, weaponTwoName, weaponTwoDamage);


    int name = builder.createString("Orc");

    // Create a `vector` representing the inventory of the Orc. Each number
    // could correspond to an item that can be claimed after he is slain.
    byte[] treasure = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int inv = Monster.createInventoryVector(builder, treasure);

    // Place the two weapons into an array, and pass it to the `createWeaponsVector()` method to
    // create a FlatBuffer vector.
    int[] weaps = new int[2];
    weaps[0] = sword;
    weaps[1] = axe;

    // Pass the `weaps` array into the `createWeaponsVector()` method to create a FlatBuffer vector.
    int weapons = Monster.createWeaponsVector(builder, weaps);

    Monster.startPathVector(builder, 2);
    Vec3.createVec3(builder, 1.0f, 2.0f, 3.0f);
    Vec3.createVec3(builder, 4.0f, 5.0f, 6.0f);
    int path = builder.endVector();

    // Create our monster using `startMonster()` and `endMonster()`.
    Monster.startMonster(builder);
    Monster.addPos(builder, Vec3.createVec3(builder, 1.0f, 2.0f, 3.0f));
    Monster.addName(builder, name);
    Monster.addColor(builder, Color.Red);
    Monster.addHp(builder, (short) 300);
    Monster.addInventory(builder, inv);
    Monster.addWeapons(builder, weapons);
    // union
    Monster.addEquippedType(builder, Equipment.Weapon);
    Monster.addEquipped(builder, axe);
    Monster.addPath(builder, path);
    int orc = Monster.endMonster(builder);
    builder.finish(orc);

    // This must be called after `finish()`.
    //java.nio.ByteBuffer buf = builder.dataBuffer();
    // The data in this ByteBuffer does NOT start at 0, but at buf.position().
    // The number of bytes is buf.remaining().

    // Alternatively this copies the above data out of the ByteBuffer for you:
    byte[] buf = builder.sizedByteArray();
    return buf;
  }

  public static void main(String[] args) {
    byte[] bytes = createMonster();
    java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(bytes);

    // Get an accessor to the root object inside the buffer.
    Monster monster = Monster.getRootAsMonster(buf);
    short hp = monster.hp();
    short mana = monster.mana();
    String name = monster.name();

    System.out.println(name);
    System.out.println(hp);
    System.out.println(mana);
  }
}