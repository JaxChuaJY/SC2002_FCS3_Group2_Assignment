package main;

/**
 * Entry point for the BTO Management application.
 */
public class main {

	// -----MAIN PROGRAM
	/**
     * Launches the BTO system, handling exceptions and
     * iterating user menus until logout.
     *
     * @param args command-line arguments (ignored)
     */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTOManagementSystem btoSys;
		try {
			btoSys = new BTOManagementSystem();
			btoSys.startSystem();
		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Catches in main");
			e.printStackTrace();
			return;

		}


		do {
			btoSys.getUserManager().getcurrentUser().showMenu(btoSys);
		}while (btoSys.getUserManager().getcurrentUser() != null);
		
			
		System.out.println("Exitting program...");

	}

}
