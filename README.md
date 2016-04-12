# tree-leveled-cipher
Asymetric-key block-cipher Encryption technique

The algorithm for the encryption and decryption is as follows :


Encryption
-----------------

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





Decryption
-------------

=>Obtain the Cipher Text;

=>Construct a balanced Tree from the Cipher Text;

=>Calculate the height h of the tree;

=> Steps to generate Decryption Key (keyD)

	->keyD = (26*N - (keyE + 26*keyFactor)) % 26
	
	(Private key = keyD)

=> In the tree formed, for every level 'l' in the tree, add the l'th keyD.

=> The Plain Text is the inorder traversal of that tree.
