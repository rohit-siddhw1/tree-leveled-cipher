/*

Tree Leveled Cipher
===================
Type : Public-Key Encryption | Variable Block Cipher.
Developed by : Rohit G. Siddheshwar BE-Comps 6787

*/

import java.util.*;

/*

Algorithm :
===========

------------------------------------------------------------------------------------
Encryption
-------------------------------------------------------------------------------------
=> Obtain Plain Text from the user;
=> Construct a balanced Binary Tree using the entered text;
=> Calculate the height of the tree (h);
=> Generate h random numbers for Major;
=> Generate h random numbers for N;

=> Steps to generate Encryption Key (keyE)
	->keyE = N*Major % 26;
	->keyFactor = N*Major / 26;
	(Public key = (keyE,keyFactor))

=> In the tree formed, for every level 'l' in the tree, add the l'th keyE.
=> The Cipher Text is the inorder traversal of that tree.
----------------------------------------------------------------------------------------



----------------------------------------------------------------------------------------
Decryption
----------------------------------------------------------------------------------------
=>Obtain the Cipher Text;
=>Construct a balanced Tree from the Cipher Text;
=>Calculate the height h of the tree;

=> Steps to generate Decryption Key (keyD)
	->keyD = (26*N - (keyE + 26*keyFactor)) % 26
	(Private key = keyD)

=> In the tree formed, for every level 'l' in the tree, add the l'th keyD.
=> The Plain Text is the inorder traversal of that tree.
----------------------------------------------------------------------------------------
*/


class TreeLeveledCipher{

	static String pt;//plain text
	static String newPt = "";//the newer plain text
	static String ct = "";//cipher text
	static String keyE = "";//encryption key
	static String keyD = "";//decryption key

	static int[] ptInt;
	static int[] ctInt;
	static int[] keyFactor;

	static int[] randomMajor;
	static int[] randomN;

	static class Node{
		int data;
		char cipher;
		Node left;
		Node right;
	}

	static Node root;
	static ArrayList<Node> levelOrder;
	static int heightOfTree;

	public static void generateRandomNos(){
		heightOfTree = heightOfBinaryTree(root);

		randomMajor = new int[heightOfTree];
		randomN = new int[heightOfTree];

		for(int i = 0; i < heightOfTree; i++){
			randomMajor[i] = ((int)(Math.random()*100)) % 25 + 1;
			randomN[i] = ((int)(Math.random()*100)) % 10 + 1;
		}
	}


	public static void convertSharedKeyToEncryptionKey(){
		keyFactor = new int[heightOfTree];
		int[] temp = new int[heightOfTree];
		for(int i=0;i<heightOfTree;i++){
			temp[i] = (randomMajor[i] * randomN[i]) % 26;
			keyFactor[i] = (randomMajor[i] * randomN[i]) / 26;
			keyE = keyE + String.valueOf((char)((int)temp[i] + (int)'a'));
		}
	}

	public static void convertSharedKeyToDecryptionKey(){
		int[] temp = new int[heightOfTree];
		for(int i=0;i<heightOfTree;i++){
			temp[i] = ((26 * randomN[i]) - (((int)keyE.charAt(i) - (int)'a') + (26*keyFactor[i]))) % 26;
			keyD = keyD + String.valueOf((char)((int)temp[i] + (int)'a'));
		}
	}

	public static Node createTree(int start, int end){
		if(start == end){
			Node n = new Node();
			n.data = ptInt[start];
			n.left = null;
			n.right = null;
			return n;
		}
		else if(end - start == 1){
			Node n = new Node();
			n.data = ptInt[start];
			n.left = null;
			n.right = createTree(end,end);
			return n;
		}
		else{
			int mid = (end - start)/2;
			Node n = new Node();
			n.data = ptInt[start + mid];
			n.left = createTree(start , start + mid-1);
			n.right = createTree(start + mid+1 , end);
			return n;
		}
	}

	public static Node createTreeD(int start, int end){
		if(start == end){
			Node n = new Node();
			n.data = ctInt[start];
			n.left = null;
			n.right = null;
			return n;
		}
		else if(end - start == 1){
			Node n = new Node();
			n.data = ctInt[start];
			n.left = null;
			n.right = createTreeD(end,end);
			return n;
		}
		else{
			int mid = (end - start)/2;
			Node n = new Node();
			n.data = ctInt[start + mid];
			n.left = createTreeD(start , start + mid-1);
			n.right = createTreeD(start + mid+1 , end);
			return n;
		}
	}

	public static int heightOfBinaryTree(Node node)
	{
	    if (node == null)
	    {
	        return 0;
	    }
	    else
	    {
	        return 1 + Math.max(heightOfBinaryTree(node.left), heightOfBinaryTree(node.right));
	    }
	}


	public static void levelOrderTraversal(Node startNode) {  
		Queue<Node> queue=new LinkedList<Node>();  
		levelOrder = new ArrayList<>();
		queue.add(startNode);  
		while(!queue.isEmpty())  
		{  
			Node tempNode=queue.poll();
			//System.out.printf("%d ",tempNode.data);
			levelOrder.add(tempNode);
			if(tempNode.left!=null)  
				queue.add(tempNode.left);  
			if(tempNode.right!=null)  
				queue.add(tempNode.right);  
		}  
	}

	public static void inOrder(Node p){
		if(p == null)
			return;
		else
		{
			inOrder(p.left);
			ct = ct + String.valueOf(p.cipher);
			inOrder(p.right);
		}
	}

	public static void inOrderPT(Node p){
		if(p == null)
			return;
		else
		{
			inOrderPT(p.left);
			newPt = newPt + String.valueOf(p.cipher);
			inOrderPT(p.right);
		}
	}

	public static int nodesOnLevel(int x){
		return (int)Math.pow(2,x);
	}


	public static void convertToIntArray(){
		ptInt = new int[pt.length()];
		for(int i = 0; i < pt.length(); i++){
			ptInt[i] = (int)((int)pt.charAt(i) - (int)'a');
		}
	}

	public static void convertToIntArrayCT(){
		ctInt = new int[ct.length()];
		for(int i = 0; i < ct.length(); i++){
			ctInt[i] = (int)((int)ct.charAt(i) - (int)'A');
		}
	}

	/*
	public static void checkKeys(){
		for(int i=0;i<heightOfTree;i++){
			System.out.println("old : " + (randomMajor[i] * randomN[i]) + "\tnew : " + (((int)keyE.charAt(i) - (int)'a') + (26*keyFactor[i])));
		}
	}
	*/

	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);

		System.out.println("|=========================================|");
		System.out.println("|========== Tree Leveled Cipher ==========|");
		System.out.println("|=========================================|");
		System.out.println("|");
		System.out.print("| Enter the Plain-Text : ");

		pt = sc.nextLine();
		pt = pt.replaceAll(" ","");
		System.out.println("|");
		System.out.println("|");

		encryptMessage();
		displayAfterEncryption();
		System.out.print("| Do you want to Decrypt the message (Y/n) : ");
		String decision = sc.nextLine();
		if(decision.equalsIgnoreCase("n")){
		}
		else{
			decryptMessage();
			displayAfterDecryption();
		}

		System.out.println("|");
		System.out.println("|=========================================|");

		//checkKeys();


	}

	public static void encryptMessage(){
		int limit;
		int k=0;

		convertToIntArray();
		System.out.println("|");
		System.out.println("| Converting to integer Array..");
		System.out.println("|");

		root = createTree(0,pt.length() - 1);

		System.out.println("|");
		System.out.println("| Created Tree..");
		System.out.println("|");

		generateRandomNos();
		convertSharedKeyToEncryptionKey();
		convertSharedKeyToDecryptionKey();
		levelOrderTraversal(root);

		System.out.println("|");
		System.out.println("| Applying encryption..");
		System.out.println("|");

		for(int i = 0;i < heightOfTree; i++){
			limit = nodesOnLevel(i);
			for(int j = 0; j < limit; j++){
				if(k < pt.length()){
					levelOrder.get(k).cipher = (char)(((levelOrder.get(k).data + ((int)keyE.charAt(i) - (int)'a')) % 26) + (int)'A');
					//ct = ct + String.valueOf(levelOrder.get(k).cipher);
					k++;
				}
			}
		}

		System.out.println("|");
		System.out.println("| Encrypted.");
		System.out.println("|");
	}

	public static void displayAfterEncryption(){
		inOrder(root);
		System.out.println("|");
		System.out.println("| Encryption KEY : " + keyE);
		System.out.println("| CT : " + ct);
		System.out.println("|");
	}

	public static void decryptMessage(){
		int limit;
		int k=0;
		root = null;
		levelOrder.clear();

		convertToIntArrayCT();

		root = createTreeD(0,ct.length() - 1);

		levelOrderTraversal(root);

		System.out.println("|");
		System.out.println("| Decrypting..");
		System.out.println("|");

		for(int i = 0;i < heightOfTree; i++){
			limit = nodesOnLevel(i);
			for(int j = 0; j < limit; j++){
				if(k < pt.length()){
					levelOrder.get(k).cipher = (char)(((levelOrder.get(k).data + ((int)keyD.charAt(i) - (int)'a')) % 26) + (int)'a');
					//newPt = newPt + String.valueOf(levelOrder.get(k).cipher);
					k++;
				}
			}
		}

		System.out.println("|");
		System.out.println("| Decrypted.");
		System.out.println("|");
	}

	public static void displayAfterDecryption(){
		inOrderPT(root);
		System.out.println("|");
		System.out.println("| Decryption KEY : " + keyD);
		System.out.println("| PT : " + newPt);
		System.out.println("|");
	}
}

/*

TEST CASES : 

1] Small Inputs : 

|=========================================|
|========== Tree Leveled Cipher ==========|
|=========================================|
|
| Enter the Plain-Text : check for small inputs
|
|
|
| Converting to integer Array..
|
|
| Created Tree..
|
|
| Applying encryption..
|
|
| Encrypted.
|
|
| Encryption KEY : ygmeo
| CT : GTIQQJAVGKEXPWTTGXG
|
| Do you want to Decrypt the message (Y/n) : y
|
| Decrypting..
|
|
| Decrypted.
|
|
| Decryption KEY : cuowm
| PT : checkforsmallinputs
|
|
|=========================================|


2] For medium Inputs

|=========================================|
|========== Tree Leveled Cipher ==========|
|=========================================|
|
| Enter the Plain-Text : this is a case of emergency admit imhideatly           
|
|
|
| Converting to integer Array..
|
|
| Created Tree..
|
|
| Applying encryption..
|
|
| Encrypted.
|
|
| Encryption KEY : otmcev
| CT : XJMEMUEXTWGSAQQGVBSRECMHOMOBQJMYQEVPT
|
| Do you want to Decrypt the message (Y/n) : y
|
| Decrypting..
|
|
| Decrypted.
|
|
| Decryption KEY : mhoywf
| PT : thisisacaseofemergencyadmitimhideatly
|
|
|=========================================|


3] For Long Inputs

|=========================================|
|========== Tree Leveled Cipher ==========|
|=========================================|
|
| Enter the Plain-Text : i dont know who you are i dont know what you want if you are looking for ransom i can tell you i dont have money but what i do have are a very particular set of skills skills i have acquired over a very long career skills that make me a nightmare for people like you if you let my family go now that will be the end of it i will not look for you i will not pursue you but if you dont i will look for you i will find you and i will kill you
|
|
|
| Converting to integer Array..
|
|
| Created Tree..
|
|
| Applying encryption..
|
|
| Encrypted.
|
|
| Encryption KEY : snvgkycqn
| CT : YFEARAPEJGXQOBSQEGYQUDVAAMMYXNDOQKJYDGKVLJKCHRJEQAVXWHEEPQAUEZOSCDGCBYAEHSTQDGFQIGCBAUARHRMJQGSTQXNTUNTUNBUTOCYHVYPEBCHFCJBHIXDBNIFIYNBFSXCLRYSDWYEKTQLRPQIGHLVEPWPYHRGHFCYNBFRXCJZKAGCRYDVIXGSQTUSMHRUBZBGBVIULQKVAOQKYCJOOSKCKBLEEAQMGNQVMVJBOGJUOUPTBDYGKMVYBPEGJEQASYHAEHGMVNBAUJRKEQKRAEHLKVYSWEHFEAOYYYYJBQEXPETOBSYJKBYLYPTLMKNPTVGYNBXGBYAEH
|
| Do you want to Decrypt the message (Y/n) : y
|
| Decrypting..
|
|
| Decrypted.
|
|
| Decryption KEY : infuqcykn
| PT : idontknowwhoyouareidontknowwhatyouwantifyouarelookingforransomicantellyouidonthavemoneybutwhatidohaveareaveryparticularsetofskillsskillsihaveacquiredoveraverylongcareerskillsthatmakemeanightmareforpeoplelikeyouifyouletmyfamilygonowthatwillbetheendofitiwillnotlookforyouiwillnotpursueyoubutifyoudontiwilllookforyouiwillfindyouandiwillkillyou
|
|
|=========================================|


*/