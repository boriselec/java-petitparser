package org.petitparser.parser.combinators;

import org.petitparser.context.Context;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import java.util.Arrays;

/**
 * A parser that uses the first parser that succeeds.
 */
public class ChoiceParser extends ListParser {

  public ChoiceParser(Parser... parsers) {
    super(parsers);
  }

  @Override
  public Result parseOn(Context context) {
    Result result = null;
    for (Parser parser : parsers) {
      result = parser.parseOn(context);
      if (result.isSuccess()) {
        return result;
      }
    }
    return result == null ? context.failure("Empty choice") : result;
  }

  @Override
  public int fastParseOn(String buffer, int position) {
    int result = -1;
    for (Parser parser : parsers) {
      result = parser.fastParseOn(buffer, position);
      if (result >= 0) {
        return result;
      }
    }
    return result;
  }

  @Override
  public ChoiceParser or(Parser... others) {
    Parser[] array = Arrays.copyOf(parsers, parsers.length + others.length);
    System.arraycopy(others, 0, array, parsers.length, others.length);
    return new ChoiceParser(array);
  }

  @Override
  public ChoiceParser copy() {
    return new ChoiceParser(Arrays.copyOf(parsers, parsers.length));
  }
}
