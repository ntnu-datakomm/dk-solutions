package no.ntnu;

/**
 * A status of a washing process.
 *
 * @param program         The program being executed
 * @param temperature     The reached temperature of the water
 * @param percentComplete Completion degree of the program, in percent
 */
public record WashingStatus(WashingProgram program, int temperature, int percentComplete) {
}
