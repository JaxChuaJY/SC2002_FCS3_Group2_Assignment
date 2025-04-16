package main;


public class main {

	// -----MAIN PROGRAM
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

			// **Add in error in reading files catch Exception error!
			// Need to make the readUsers throw Exception but that is if wanted.
			// It already throws an exception but DOES NOT end program.
		}


		do {
			btoSys.getUserManager().getcurrentUser().showMenu(btoSys);
		}while (btoSys.getUserManager().getcurrentUser() != null);
		
			
		System.out.println("Exitting program...");

	}

}
