Smart ATM Simulator is a console program in Java that imitates an ATM. It started as a short Python withdrawal script from your video and was extended with cardless withdrawal and multiple accounts.
What it does
Main menu: You choose a card withdrawal, a cardless withdrawal (OTP), or exit. After each transaction it asks whether you want another.
Cardless withdrawal:
You enter a 10-digit cellphone number.
The program generates a random 6-digit code and prints a simulated SMS.
You get 3 attempts to enter it, and it expires after 60 seconds.
A correct code lets you continue. Otherwise the transaction is cancelled.
Accounts: Savings (R500), Cheque (R1200) and Credit (R2000 available credit). Each keeps its own balance, and balances carry over between transactions.
Withdrawal rules:
An amount of R0 or less is rejected.
An amount within the balance succeeds and shows the remaining balance.
An amount over the balance is refused as insufficient funds.
How it's built
An Account class holds the name, balance and whether it's a credit account.
verifyOtp() handles the phone number, code generation, expiry and attempt limit.
withdraw() handles account selection and the withdrawal rules.
readInt() and readDouble() stop the program crashing when someone types letters instead of numbers.
Limitations
The OTP is printed on screen rather than sent by SMS.
There is no PIN, so card withdrawals go straight to account selection.
Balances reset every time the program restarts.
Credit is treated as a simple available-credit amount, with no interest or amount owed.
