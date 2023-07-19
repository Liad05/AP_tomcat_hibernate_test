package test;

import java.io.*;

public class BookScrabbleHandler implements ClientHandler {
	BufferedReader in;
	PrintWriter out;
	@Override
	
	public void handleClient(InputStream inFromclient, OutputStream outToClient) {
		// we need to take the first letter, decide if its 'C' or 'Q' and activate the corresponding function
		// outToClient will contain the result 'true' or 'false'
		
		in=new BufferedReader(new InputStreamReader(inFromclient));
		out=new PrintWriter(outToClient,true);
		try {
			String line=in.readLine();
			String[] parts = line.split(",");
			String action=parts[0]; // now action contains "C" or "Q"
			String[] newArray = new String[parts.length - 1];
			System.arraycopy(parts, 1, newArray, 0, newArray.length);// newArray contains all the other parameters
			DictionaryManager dm=DictionaryManager.get();
			boolean res=false;
			if(action.equals("Q"))
			{ //we need to activate query
				res=dm.query(newArray);
			}
			else {
				if(action.equals("C"))
				{ //we need to activate challenge
					res=dm.challenge(newArray);
				}
			}
			out.println(res);

			inFromclient.close();
			outToClient.close();
						

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.close();
		
	}
	
}