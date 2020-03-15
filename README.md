# Spring Integration

- **SFTP**
  - **Download**
  
---

### HOW TO GENERATE KEYS

```
>$ ssh-keygen -t rsa -m PEM
 Generating public/private rsa key pair.
 Enter file in which to save the key (/Users/rommelmedina/.ssh/id_rsa): ./sftp_rsa
 Enter passphrase (empty for no passphrase): 
 Enter same passphrase again: 
 Your identification has been saved in ./sftp_rsa.
 Your public key has been saved in ./sftp_rsa.pub.
 The key fingerprint is:
 4d:2c:a5:b0:2c:58:2c:c6:f0:4c:24:b9:72:fd:71:df rommelmedina@192.168.100.10
 The key's randomart image is:
 +--[ RSA 2048]----+
 |..o*.  ...       |
 |o+o.Bo  o        |
 |oo  ==.o .       |
 |  . .+..o E      |
 |   .  . S. .     |
 |       .         |
 |                 |
 |                 |
 |                 |
 +-----------------+
>$ ls
 sftp_rsa	sftp_rsa.pub
>$

```

-   Create a file ~/.ssh/authorized_keys and give it the required permissions
    
    > $ chmod 600 ~/.ssh/authorized_keys
    
-   Then edit  **authorized_keys**  file and paste in the contents of the public key file  **sftp_rsa.pub**.
    
-   Move your private key file  **sftp_rsa**  to the directory  **/src/main/resources/key/**  directory.

**References:** 

- [Spring Integration: SFTP Download Using Key-Based Authentication](https://dzone.com/articles/spring-integration-sftp-download-using-key-based-a)
- [github.com:spring-projects/spring-integration-samples](https://github.com/spring-projects/spring-integration-samples/tree/master/basic/sftp)