package OOPsProject;
import java.io.*;
import java.util.Scanner;
class ATM {
        private String username;
        private int password;
        private int balance;

        public String getUsername() {
            return username;
        }

        public int getPassword() {
            return password;
        }

        public void getData(Scanner sc) {
            sc.nextLine(); // Clear the buffer
            System.out.println("\nEnter username:");
            username = sc.nextLine();
            System.out.println("\nEnter 4-digit password:");
            password = sc.nextInt();
            System.out.println("\nEnter initial balance:");
            balance = sc.nextInt();
        }

        public void showData() {
            System.out.println("Username: " + username + ", Password: " + password + ", Balance: " + balance);
        }

        public void addUser(Scanner sc) {
            try (FileOutputStream fos = new FileOutputStream("aaa.ser", true);
                 ObjectOutputStream oos = fos.getChannel().position() == 0 ? new ObjectOutputStream(fos) : new AppendingObjectOutputStream(fos)) {
                getData(sc);
                oos.writeObject(this);
            } catch (IOException e) {
                System.out.println("Error in creating file..");
            }
        }

        public void viewAllUsers() {
            try (FileInputStream fis = new FileInputStream("aaa.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        user.showData();
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in opening file..");
            }
        }

        public void deleteUser(String uname) {
            File originalFile = new File("aaa.ser");
            File tempFile = new File("temp.ser");

            try (FileInputStream fis = new FileInputStream(originalFile);
                 ObjectInputStream ois = new ObjectInputStream(fis);
                 FileOutputStream fos = new FileOutputStream(tempFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        if (!user.getUsername().equals(uname)) {
                            oos.writeObject(user);
                        } else {
                            System.out.println("Data found and deleted\n" + user.username + "\n");
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in processing file..");
            }

            if (!originalFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Could not rename file");
            }
        }

        public void updateUserAsDeposit(String uname, Scanner sc) {
            File originalFile = new File("aaa.ser");
            File tempFile = new File("temp.ser");

            try (FileInputStream fis = new FileInputStream(originalFile);
                 ObjectInputStream ois = new ObjectInputStream(fis);
                 FileOutputStream fos = new FileOutputStream(tempFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        if (user.getUsername().equals(uname)) {
                            System.out.println("\nEnter amount to deposit:");
                            int amount = sc.nextInt();
                            user.balance += amount;
                            System.out.println("\nBalance is: " + user.balance);
                        }
                        oos.writeObject(user);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in processing file..");
            }

            if (!originalFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Could not rename file");
            }
        }

        public void updateUserAsWithdraw(String uname, Scanner sc) {
            File originalFile = new File("aaa.ser");
            File tempFile = new File("temp.ser");

            try (FileInputStream fis = new FileInputStream(originalFile);
                 ObjectInputStream ois = new ObjectInputStream(fis);
                 FileOutputStream fos = new FileOutputStream(tempFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        if (user.getUsername().equals(uname)) {
                            System.out.println("\nEnter amount to withdraw:");
                            int amount = sc.nextInt();
                            if (user.balance < amount) {
                                System.out.println("\nInsufficient balance to withdraw");
                            } else {
                                user.balance -= amount;
                                System.out.println("\nBalance is: " + user.balance);
                            }
                        }
                        oos.writeObject(user);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in processing file..");
            }

            if (!originalFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Could not rename file");
            }
        }

        public boolean searchSpecificUser(String uname, int pass) {
            try (FileInputStream fis = new FileInputStream("aaa.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        if (user.getUsername().equals(uname) && user.getPassword() == pass) {
                            return true;
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in opening file..");
            }
            return false;
        }

        public void searchAllUserToDisplay(String uname) {
            try (FileInputStream fis = new FileInputStream("aaa.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                ATM user;
                while (true) {
                    try {
                        user = (ATM) ois.readObject();
                        if (user.getUsername().equals(uname)) {
                            user.showData();
                            return;
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error in opening file..");
            }
        }
    }

    class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    public class ATMUser {
        public static void main(String[] args) {
            ATM atm = new ATM();
            Scanner sc = new Scanner(System.in);
            String uname;
            int pass, ch, ch1, ch2;
            boolean found;

            mainmenu:
            while (true) {
                System.out.println("\nWelcome to DigitalClock ATM");
                System.out.println("Login as :\n1. Admin\n2. User\n3. Exit\nChoose one : ");
                ch = sc.nextInt();

                switch (ch) {
                    case 1:
                        while (true) {
                            System.out.println("\nEnter details to login as Admin");
                            System.out.println("Enter password:");
                            pass = sc.nextInt();
                            if (pass == 1234) {
                                while (true) {
                                    System.out.println("\nWelcome to Admin Menu");
                                    System.out.println("1. Add User\n2. Delete User\n3. View All User\n4. Exit");
                                    System.out.println("Select one : ");
                                    ch1 = sc.nextInt();
                                    switch (ch1) {
                                        case 1:
                                            atm.addUser(sc);
                                            break;
                                        case 2:
                                            System.out.println("\nEnter the Username to be deleted : ");
                                            sc.nextLine(); // Clear the buffer
                                            uname = sc.nextLine();
                                            atm.deleteUser(uname);
                                            break;
                                        case 3:
                                            atm.viewAllUsers();
                                            break;
                                        case 4:
                                            continue mainmenu;
                                    }
                                }
                            } else {
                                System.out.println("Details are incorrect! Please try again");
                            }
                        }
                    case 2:
                        System.out.println("\nEnter details to login as User");
                        sc.nextLine(); // Clear the buffer
                        System.out.println("Enter username:");
                        uname = sc.nextLine();
                        System.out.println("Enter password:");
                        pass = sc.nextInt();
                        found = atm.searchSpecificUser(uname, pass);

                        if (found) {
                            while (true) {
                                System.out.println("\nWelcome " + uname);
                                System.out.println("1. Deposit\n2. Withdraw\n3. View Account\n4. Exit\nEnter your choice:");
                                ch2 = sc.nextInt();

                                switch (ch2) {
                                    case 1:
                                        atm.updateUserAsDeposit(uname, sc);
                                        break;
                                    case 2:
                                        atm.updateUserAsWithdraw(uname, sc);
                                        break;
                                    case 3:
                                        atm.searchAllUserToDisplay(uname);
                                        break;
                                    case 4:
                                        System.out.println("Thank you");
                                        continue mainmenu;
                                }
                            }
                        } else {
                            System.out.println("No account found with username: " + uname);
                        }
                        break;
                    case 3:
                        System.out.println("\nThank you for banking with XYZ bank India");
                        break mainmenu;
                }
            }
            sc.close();
        }
    }


