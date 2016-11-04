package tr.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script {

	public Script() {

	}

	public String evaluateString(String expression) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object result = null;
		try {
			result = engine.eval(expression);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		String finalString = String.valueOf(result);
		return finalString;
	}
}
