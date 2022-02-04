package it.contocorrente.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.contocorrente.entity.Contocorrente;
import it.contocorrente.entity.Movimento;
import it.contocorrente.entity.Operazione;


@Path("/conto")
public class ContocorrenteRest {

	private static ArrayList<Contocorrente> conti = new ArrayList<>();
	private static ArrayList<Movimento> movimenti = new ArrayList<>(); 

	//http://localhost:8080/EWallet/rest/conto


	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaConto(Contocorrente c) {
		conti.add(c);

		return Response.status(200).entity("Creazione conto avvenuta con successo!").build();

	}

	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancellaConto(Contocorrente c) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(c.getIban())) {
				conti.remove(con);
				return Response.status(200).entity("Conto rimosso correttamente!").build();
			}

		}
		return Response.status(404).entity("Un conto con questo Iban non e presente!").build();

	}

	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response aggiornoConto(Contocorrente c) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(c.getIban())) {
				int index = conti.lastIndexOf(con);
				conti.set(index, c);
				return Response.status(200).entity("Conto aggiornato correttamente!").build();
			}
		}
		return Response.status(404).entity("Un conto con questo Iban non esiste!").build();
	}

	@PUT
	@Path("/movimento")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response preleva(Movimento m) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(m.getIban())) {

				if (m.getTipo().equals(Operazione.PRELIEVO)) {
					if (m.getImporto() > con.getSaldo() || m.getImporto() < 0) {
						return Response.status(406).entity("Non puoi prelevare piu del tuo saldo attuale e/o selezionare un importo negativo").build();
					}
					double nuovoSaldo = con.getSaldo()- m.getImporto();
					con.setSaldo(nuovoSaldo);
					movimenti.add(m);
					return Response.status(200).entity("Operazione effettuata! il nuovo saldo e: "+ nuovoSaldo ).build();
				}

				if (m.getTipo().equals(Operazione.VERSAMENTO)) {
					if(m.getImporto() < 0) {
						return Response.status(406).entity("Non puoi versare un importo negativo").build();
					}
					double nuovoSaldo = con.getSaldo() + m.getImporto();
					con.setSaldo(nuovoSaldo);
					movimenti.add(m);
					return Response.status(200).entity("Operazione effettuata! il nuovo saldo e: "+ nuovoSaldo ).build();
				}
			}
		}
		return Response.status(404).entity("ERRORE! Tipo operazione non valido").build();
	}


	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimento> getMovimenti(){

		return movimenti;
	}

	@GET
	@Path("/conti")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contocorrente> getConti(){

		return conti;
	}

}
