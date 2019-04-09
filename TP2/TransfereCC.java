import java.net.*;
import java.io.*;

public class TransfereCC extends Thread {
    AgenteUDP agente;
    private final boolean upload;
    private final boolean download;
    private File fich;
    private String filename;
    String destinationIP;

    public TransfereCC(File f) throws SocketException,Exception{
        agente = new AgenteUDP(this);
        this.upload = true;
        this.download = false;
        this.fich = f;
        this.filename = f.getName();
        destinationIP = "";
    }

    public TransfereCC(String file, String destip) throws SocketException,Exception{
        this.agente = new AgenteUDP(this);
        this.upload = false;
        this.download = true;
        this.fich = null;
        this.filename = file;
        destinationIP = destip;
    }

    public void recebePDU(PDU p){
        // tranformar em pdu
        // alterar estado
        /**try{
            agente.sendPDU(p.getData(),InetAddress.getByName(p.getSourceIP()),p.getSourcePort());
        } catch(Exception e){
            e.printStackTrace();
        }*/
    }

    public void run(){
        try{
            // inicializa server que recebe packets
            Thread tagent = new Thread(agente);
            tagent.start();

            if(this.download == true)
                new Thread(new TransfereCCDownload(agente,destinationIP)).run();

            tagent.interrupt();
        } catch(UnknownHostException e){
            e.printStackTrace();
        }

    }
}

class TransfereCCDownload extends Thread{
    AgenteUDP agente;
    private InetAddress addressDest;

    public TransfereCCDownload(AgenteUDP agent, String destip) throws UnknownHostException{
        agente = agent;
        this.addressDest = InetAddress.getByName(destip);
    }

    public void run(){
        PDU p = new PDU(0, 0, 1024, false, false, false, true,"OLA");
        agente.sendPDU(p,addressDest,7777);
    }
}
