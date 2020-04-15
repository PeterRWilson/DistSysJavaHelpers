package hu.mta.sztaki.lpds.cloud.simulator.helpers.trace;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;
import hu.mta.sztaki.lpds.cloud.simulator.helpers.trace.file.TraceFileReaderFoundation;

public class PreziReader extends TraceFileReaderFoundation {

	TraceFileReaderFoundation TFRF;

	public PreziReader(String fileName, int from, int to, boolean furtherjobs, Class<? extends Job> jobType)
			throws SecurityException, NoSuchMethodException {
		super("Grid workload format", fileName, from, to, furtherjobs, jobType);
	}

	// This methods checks to see what the file ends in the following and splits it
	protected boolean isTraceLine(final String param) {
		String[] test = param.split(" ");

		if (test[0].equals(String.valueOf(0))) {
			if (test[1].equals(String.valueOf(1))) {
				if (test[3].equalsIgnoreCase("default")) {
				} else if (test[3].equalsIgnoreCase("url")) {
				} else if (test[3].equalsIgnoreCase("export")) {

				}
			}
		}

		if (true) {
			return true;
		} else
			return false;
	}

	protected void metaDataCollector(String data) {

	}

	public Job createJobFromLine(String jobstring)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		jobstring.endsWith("ASKALON");
		String[] elements = jobstring.trim().split("\\s+");

		if (elements[2].contains("error:unsupported-request-method")) {
			return null;
		}
		long jobState = Long.parseLong(elements[0]);
		// Hard coded data values for testing purposes
		int procs = ThreadLocalRandom.current().nextInt(1, 4);
		long runtime = 400;
		long waitTime = 0;
		String name = elements[2];

		String[] values = name.split("w");
		String desired = values[0];
		// object name = object name.substring(0, endIndex)
		desired = desired.substring(0, desired.length() - 1);

		String submitTime = elements[1];
		submitTime = submitTime.substring(0, submitTime.indexOf("."));
		long submitToLong = Long.parseLong(submitTime);

		if (jobState != 1 && (procs < 1 || runtime < 0)) {
			return null;
		} else {
			return jobCreator.newInstance(
					// Gets id from log files first element
					elements[0],
					// Gets submit time:
					submitToLong,
					// Gets the queueing time:
					Math.max(0, waitTime),
					// Gets the execution time:
					Math.max(0, runtime),
					// Gets number of processors
					Math.max(1, procs),
					// Gets average execution time
					300,
					// no memory
					300,
					// Gets for user name:
					parseTextualField(desired),
					// Gets for group membership:
					parseTextualField(desired),
					// Gets executable name:
					parseTextualField(desired),
					// No preceding job
					null, 0);
		}
	}

	// check to see if the unparsed data
	// matches("^-?[0-9](?:\\.[0-9])?$")?"N/A":unparsed;
	private String parseTextualField(final String unparsed) {
		return unparsed.equals("-1") ? "N/A" : unparsed;

	}
}
