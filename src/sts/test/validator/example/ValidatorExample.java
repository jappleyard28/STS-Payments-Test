/* Copyright (c) 2015-2018 Smart Technology Solutions Limited. All Rights
 * Reserved.
 *
 * This software is the confidential and proprietary information of Smart
 * Technology Solutions Limited. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Smart
 * Technology Solutions Limited.
 *
 * SMART TECHNOLOGY SOLUTIONS LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES
 * ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SMART TECHNOLOGY SOLUTIONS LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. */

package sts.test.validator.example;

import sts.test.validator.Validator;
import java.util.List;
import java.util.ArrayList;

/**
 * Example implementation of {@link Validator}. Candidates may modify this class
 * or write their own implementation.
 */
public class ValidatorExample
        implements Validator {

  /* Note: the required no-argument constructor is implicitly defined if no
   * other constructors are provided */

  /**
   * Indicate if the given message is valid.
   *
   * @param message The message to check
   * @return {@code true} if the message starts with STX, ends with ETX and the
   *         correct LRC, and has correctly-escaped data; {@code false}
   *         otherwise.
   * @todo Implement this method
   */
  // STX - 2 (0x02)
  // DLE - 16 (0x10)
  // ETX - 3 (0x03)
  // lrc - 20 (0x14)
  public boolean isValid(byte[] message) {
    boolean valid = true;
    // check length is greater than 3 (STX, ETX, lrc)
    if (message.length <= 3) {
      valid = false;
    }
    // check starts with STX
    if (message[0] != 0x02) {
      valid = false;
    }
    // check ends with ETX
    if (message[message.length-2] != 0x03) {
      valid = false;
    }

    if (valid) {
      boolean dle = false; // used to check if each data byte is a dle
      byte messageLrc = message[message.length - 1];

      // extract data from message
      List<Byte> data = new ArrayList<Byte>(); // stores the data from the message
      // check for dle by seeing if there is a 0x10 (16) before a 2, 3, or 16
      for (int i = 1; i < message.length - 2; i++) {
        if (i + 1 != message.length) {
          if ((message[i] == 0x10 && message[i + 1] == 0x02) || (message[i] == 0x10 && message[i + 1] == 0x03) || (message[i] == 0x10 && message[i + 1] == 0x10)) {
            dle = true;
          }
        }
        if (!dle) {
          data.add(message[i]);
          System.out.println(message[i]);
        }
        dle = false;
      }

      // calculate lrc
      byte lrc = 0; // lrc I calculate
      System.out.println("-------------------------");
      for (byte b : data) {
        lrc ^= b;
      }

      // check if lrcs match
      if (messageLrc != lrc) {
        valid = false;
      }
    }
    return valid;
  }
}
