# Cryptocurrency Exchange Application


## About
This is a Java application developed on the idea of Blockchain technology, which helps in maintaining a secure cryptocurrency exchange account for the users. The users are authenticated through a Zero Knowledge Proof problem. For a cryptocurrency transaction, the Elliptic Curve Cryptography algorithm is used to generate each user’s private and public keys in a KeyPair. The transactions between two users are then hashed with the public key of the organization and the transaction block is added to the history of transactions under the user authenticated. The transaction is signed through the sender’s digital signature which is hashed using private key of the sender.

## Steps to run
1.	Install latest Java Development Kit (JDK) and configure the environment variables.
2.	Install eclipse and configure it to compile Java
3.	Download Bouncy Castle Jar from https://www.bouncycastle.org/download/bcprov-jdk15on-159.jar .
4.	Import it on eclipse. Open Windows >preferences in the Eclipse menu, and navigate to the Java>Build path > User Libraries tab. Click new and enter a new User Library name: like “bouncycastle_lib” and hit ok. With “bouncycastle_lib” selected press Add External JARs.Click Apply and Close.
5.	Also add the class path for the external jar.
6.	Go to File->Open Projects from file System->Directory->choose the src folder.
7.	Use the run Button to run the code.
