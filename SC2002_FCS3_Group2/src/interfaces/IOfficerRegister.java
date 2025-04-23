package interfaces;

/**
 * Defines registration actions for HDB Officers.
 */
public interface IOfficerRegister {
	/**
     * Registers an HDB Officer to a BTO project.
     */
	void registerProject();
	
	/**
     * Retrieves current registration status for the officer.
     *
     * @return a string describing registration status
     */
	String viewRegistration();
}
