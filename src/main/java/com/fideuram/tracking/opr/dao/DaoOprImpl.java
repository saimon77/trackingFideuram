package com.fideuram.tracking.opr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Beneficiario;
import com.fideuram.tracking.opr.dto.Elaborazione;
import com.fideuram.tracking.opr.dto.ElencoStati;
import com.fideuram.tracking.opr.dto.Pagamento;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;
import com.fideuram.tracking.opr.dto.Soggetto;
import com.fideuram.tracking.opr.dto.TipoStato;
import com.fideuram.tracking.opr.utility.Constant;

@Repository
public class DaoOprImpl extends DaoAbstract implements DaoOpr {

	public Logger logger = Logger.getLogger(DaoOprImpl.class);

//	String url = "jdbc:sqlserver://fvdsitfv01c.fidevita.bancafideuram.it:1433;databaseName=PUC_C";
//	String url = "jdbc:jtds:sqlserver://fvdsitfv01c.fidevita.bancafideuram.it:1433;DatabaseName=PUC_C";
//	String user = "puc001";
//	String password = "puc0010";
	@Autowired
	@Qualifier("getProperties")
	private Properties prop;

//	vista
	public List<Elaborazione> getUltimeElaborazioni(String polizza,String cf, String categoria, String sottoCategoria)
			throws Exception {

		Connection conn = null;
		PreparedStatement st = null;
		List<Elaborazione> list = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			if (polizza!=null) {
				logger.info("getUltimeElaborazioni caso 1");
				st = conn.prepareStatement( "select et.* ,topr.*,st.*, null stato_precedente,rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
						+ "	from puc001.TRK_ElaborazioneTracking_fe as et  "
						+ " left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD"
						+ "		join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
						+ "		join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
						+ "		join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
						+ "	where   et.Polizza=?  ");
				st.setString(1, polizza);
			}
			else if (categoria == null && sottoCategoria == null) {
				logger.info("getUltimeElaborazioni caso 2");
				st = conn.prepareStatement(
						"select et.* ,topr.*,st.*,sp.maxStato as stato_precedente, rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
								+ "	from puc001.TRK_ElaborazioneTracking_FE as et  "
								+ "		left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD "
								+ "		 join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
								+ "		 left join puc001.TRK_StatoPrecedete sp on (et.TipoOperazioneTrackingiD in ('2','3','5','15','16','17','18','19','22','23','24','27','28','29','30','32','33') and sp.Polizza=et.Polizza and sp.idOP=et.TipoOperazioneTrackingiD and  et.DataRichiesta=sp.DataRichiesta) or (et.TipoOperazioneTrackingiD not in ('2','3','5','15','16','17','18','19','22','23','24','27','28','29','30','32','33') and sp.Polizza=et.Polizza and sp.idOP=et.TipoOperazioneTrackingiD and et.NumeroPratica=sp.NumeroPratica)"
								+ "		 join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
								+ "		 join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
								+ "	where UPPER(et.Contraente)=?  "
				);

				st.setString(1, cf.toUpperCase());
//				st.setString(2, cf.toUpperCase());
			} else {
				logger.info("getUltimeElaborazioni caso 3");
				st = conn.prepareStatement(
						"select et.* ,topr.*,st.*,sp.maxStato as stato_precedente, rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
								+ "	from puc001.TRK_ElaborazioneTracking_FE as et  "
								+ "		left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD "
								+ "		  join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
								+ "		 left join puc001.TRK_StatoPrecedete sp on (et.TipoOperazioneTrackingiD in ('2','3','5','15','16','17','18','19','22','23','24','27','28','29','30','32','33') and sp.Polizza=et.Polizza and sp.idOP=et.TipoOperazioneTrackingiD and  et.DataRichiesta=sp.DataRichiesta) or (et.TipoOperazioneTrackingiD not in ('2','3','5','15','16','17','18','19','22','23','24','27','28','29','30','32','33') and sp.Polizza=et.Polizza and sp.idOP=et.TipoOperazioneTrackingiD and et.NumeroPratica=sp.NumeroPratica)"
								+ "		 join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
								+ "		 join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
								+ "	where UPPER(et.Contraente)=? and UPPER(topr.TipoCategoria)=? and UPPER(topr.TipoSottoCategoria)=? "
				);

				st.setString(1, cf.toUpperCase());
				st.setString(2, categoria.toUpperCase());
				st.setString(3, sottoCategoria.toUpperCase());
//			st.setString(4, cf.toUpperCase());
//			st.setString(5, categoria.toUpperCase());
//			st.setString(6, sottoCategoria.toUpperCase());
			}
			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				list = new ArrayList<Elaborazione>();
				Elaborazione ela = null;
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					ela = new Elaborazione();
					ela.setElaborazioneID(rs.getInt(Elaborazione.Campi.ELABORAZIONEID));
					ela.setPolizza(rs.getString(Elaborazione.Campi.POLIZZA));
					ela.setContraente(rs.getString(Elaborazione.Campi.CONTRAENTE));
					ela.setTipoOperazioneTrackingID(rs.getInt(Elaborazione.Campi.TIPOOPERAZIONETRACKINGID));
					ela.setTipoStatoTrackingID(rs.getInt(Elaborazione.Campi.TIPOSTATOTRACKINGID));
					ela.setStatoSistema(rs.getString(Elaborazione.Campi.STATOSISTEMA));
					ela.setCanale(rs.getString(Elaborazione.Campi.CANALE));
					ela.setNumeroPratica(rs.getString(Elaborazione.Campi.NUMEROPRATICA));
					ela.setCodiceProdotto(rs.getString(Elaborazione.Campi.CODICEPRODOTTO));
					ela.setModalitaPagamento(rs.getString(Elaborazione.Campi.MODALITAPAGAMENTO));
					ela.setDataStimaFinire(rs.getDate(Elaborazione.Campi.DATASTIMAFINIRE));
					ela.setImportoOperazione(rs.getBigDecimal(Elaborazione.Campi.IMPORTOOPERAZIONE));
					ela.setIban(rs.getString(Elaborazione.Campi.IBAN));
					ela.setContoBancario(rs.getString(Elaborazione.Campi.CONTOBANCARIO));
					ela.setDocumentiMancanti(rs.getBoolean(Elaborazione.Campi.ISDOCUMENTIMANCANTI));
					ela.setDataEstrazione(rs.getDate(Elaborazione.Campi.DATAESTRAZIONE));
//					ela.setDataAggiornamento(rs.getDate(Elaborazione.Campi.DATAAGGIORNAMENTO));
					ela.setOperazioneID(rs.getInt(Elaborazione.Campi.OPERAZIONEID));
					ela.setOperazione(rs.getString(Elaborazione.Campi.OPERAZIONE));
					ela.setPratica(rs.getBoolean(Elaborazione.Campi.ISPRATICA));
					ela.setTipoCategoria(rs.getString(Elaborazione.Campi.TIPOCATEGORIA));
					ela.setTipoSottoCategoria(rs.getString(Elaborazione.Campi.TIPOSOTTOCATEGORIA));
					ela.setBeneficiario(rs.getBoolean(Elaborazione.Campi.ISBENEFICIARIO));
					ela.setAttiva(rs.getBoolean(Elaborazione.Campi.ISATTIVA));
					ela.setStatoID(rs.getInt(Elaborazione.Campi.STATOID));
					ela.setStato(rs.getString(Elaborazione.Campi.STATO));
					ela.setOrdine(rs.getInt(Elaborazione.Campi.ORDINE));
					ela.setVisibile(rs.getBoolean(Elaborazione.Campi.ISVISIBILE));
					ela.setStato_precedente(rs.getInt(Elaborazione.Campi.STATOPRECEDENTE));
					ela.setDataRichiesta(rs.getDate(Elaborazione.Campi.DATARICHIESTA));
					ela.setIdProdotto(rs.getString(Elaborazione.Campi.ID_PRODOTTO));
					ela.setDescrizioneStatoElaborazione(rs.getString(Elaborazione.Campi.DESCRIZIONE_STATO_SISTEMA));
					ela.setImportoRichiesto(rs.getBigDecimal(Elaborazione.Campi.IMPORTO_RICHIESTO));
					list.add(ela);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero delle ultime elaborazioni");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		logger.info("trovati elementi: " + (list == null ? "0" : list.size()));
		return list;
	}

//storico
	public List<Elaborazione> getDettaglioElaborazione(String cf, String categoria, String sottoCategoria,
			String polizza, String numeroPratica, String dataRichiesta,int tipoOperazioneTrackingID) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		List<Elaborazione> list = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			String sql = "";
			if (categoria == null && sottoCategoria == null && numeroPratica != null) {
				logger.info("getDettaglioElaborazione caso 1");
				sql = "select et.* ,topr.*,st.*, rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
						+ "	from puc001.TRK_ElaborazioneTracking as et  "
						+ " left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD"
						+ "		join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
						+ "		join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
						+ "		join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
						+ "	where UPPER(et.Contraente)=?  and et.Polizza=? "
						+ (numeroPratica != null ? "and et.NumeroPratica=? " : "and et.DataRichiesta=? and et.TipoOperazioneTrackingID=?"
						);
				st = conn.prepareStatement(sql);
				st.setString(1, cf.toUpperCase());
				st.setString(2, polizza);
				if (numeroPratica != null)
					st.setString(3, numeroPratica);
				else
					st.setString(3, dataRichiesta);
				st.setInt(3, tipoOperazioneTrackingID);
			} else if (categoria == null && sottoCategoria == null && numeroPratica == null && dataRichiesta == null
					&& cf == null) {
				logger.info("getDettaglioElaborazione caso 2");
				sql = "select et.* ,topr.*,st.*, rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
						+ "	from puc001.TRK_ElaborazioneTracking_fe as et  "
						+ " left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD"
						+ "		join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
						+ "		join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
						+ "		join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
						+ "	where   et.Polizza=?   and et.TipoOperazioneTrackingID=?"
				;
				st = conn.prepareStatement(sql);
				st.setString(1, polizza);
				st.setInt(2, tipoOperazioneTrackingID);

			} else {
				logger.info("getDettaglioElaborazione caso 3");
				sql = "select et.* ,topr.*,st.*, rel.Ordine,rel.isVisibile,ob.PercentualeBeneficio as Perc_beneficio "
						+ "	from puc001.TRK_ElaborazioneTracking as et  "
						+ " left join puc001.TRK_Operazioni_Beneficiari as ob on ob.polizzaID=et.polizza and ob.OperazioneID=et.TipoOperazioneTrackingiD"
						+ "		join puc001.TRK_TipoOperazioneTracking as topr on topr.OperazioneID=et.TipoOperazioneTrackingiD "
						+ "	    join puc001.TRK_TipoStatoTracking st on et.TipoStatoTrackingID=st.StatoID  "
						+ "		join puc001.TRK_RelStatiOperazione as rel on rel.TipoOperazioneTrackingiD=et.TipoOperazioneTrackingiD and rel.TipoStatoTrackingID=et.TipoStatoTrackingID "
						+ "	where UPPER(et.Contraente)=? and UPPER(topr.TipoCategoria)=? and UPPER(topr.TipoSottoCategoria)=? and et.Polizza=? "
						+ (numeroPratica != null ? "and et.NumeroPratica=?" : "and et.DataRichiesta=?  "
							)+" and et.TipoOperazioneTrackingID=?";
				st = conn.prepareStatement(sql);
				st.setString(1, cf.toUpperCase());
				st.setString(2, categoria.toUpperCase());
				st.setString(3, sottoCategoria.toUpperCase());
				st.setString(4, polizza);
				if (numeroPratica != null)
					st.setString(5, numeroPratica);
				else
					st.setString(5, dataRichiesta);
				st.setInt(6, tipoOperazioneTrackingID);
			}
			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				list = new ArrayList<Elaborazione>();
				Elaborazione ela = null;
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					ela = new Elaborazione();
					ela.setElaborazioneID(rs.getInt(Elaborazione.Campi.ELABORAZIONEID));
					ela.setPolizza(rs.getString(Elaborazione.Campi.POLIZZA));
					ela.setContraente(rs.getString(Elaborazione.Campi.CONTRAENTE));
					ela.setTipoOperazioneTrackingID(rs.getInt(Elaborazione.Campi.TIPOOPERAZIONETRACKINGID));
					ela.setTipoStatoTrackingID(rs.getInt(Elaborazione.Campi.TIPOSTATOTRACKINGID));
					ela.setStatoSistema(rs.getString(Elaborazione.Campi.STATOSISTEMA));
					ela.setCanale(rs.getString(Elaborazione.Campi.CANALE));
					ela.setNumeroPratica(rs.getString(Elaborazione.Campi.NUMEROPRATICA));
					ela.setCodiceProdotto(rs.getString(Elaborazione.Campi.CODICEPRODOTTO));
					ela.setModalitaPagamento(rs.getString(Elaborazione.Campi.MODALITAPAGAMENTO));
					ela.setDataStimaFinire(rs.getDate(Elaborazione.Campi.DATASTIMAFINIRE));
					ela.setImportoOperazione(rs.getBigDecimal(Elaborazione.Campi.IMPORTOOPERAZIONE));
					ela.setIban(rs.getString(Elaborazione.Campi.IBAN));
					ela.setContoBancario(rs.getString(Elaborazione.Campi.CONTOBANCARIO));
					ela.setDocumentiMancanti(rs.getBoolean(Elaborazione.Campi.ISDOCUMENTIMANCANTI));
					ela.setDataEstrazione(rs.getDate(Elaborazione.Campi.DATAESTRAZIONE));
//					ela.setDataAggiornamento(rs.getDate(Elaborazione.Campi.DATAAGGIORNAMENTO));
					ela.setOperazioneID(rs.getInt(Elaborazione.Campi.OPERAZIONEID));
					ela.setOperazione(rs.getString(Elaborazione.Campi.OPERAZIONE));
					ela.setPratica(rs.getBoolean(Elaborazione.Campi.ISPRATICA));
					ela.setTipoCategoria(rs.getString(Elaborazione.Campi.TIPOCATEGORIA));
					ela.setTipoSottoCategoria(rs.getString(Elaborazione.Campi.TIPOSOTTOCATEGORIA));
					ela.setBeneficiario(rs.getBoolean(Elaborazione.Campi.ISBENEFICIARIO));
					ela.setAttiva(rs.getBoolean(Elaborazione.Campi.ISATTIVA));
					ela.setStatoID(rs.getInt(Elaborazione.Campi.STATOID));
					ela.setStato(rs.getString(Elaborazione.Campi.STATO));
					ela.setOrdine(rs.getInt(Elaborazione.Campi.ORDINE));
					ela.setVisibile(rs.getBoolean(Elaborazione.Campi.ISVISIBILE));
					ela.setStato_precedente(null);
					ela.setDataRichiesta(rs.getDate(Elaborazione.Campi.DATARICHIESTA));
					ela.setIdProdotto(rs.getString(Elaborazione.Campi.ID_PRODOTTO));
					ela.setDescrizioneStatoElaborazione(rs.getString(Elaborazione.Campi.DESCRIZIONE_STATO_SISTEMA));
					ela.setImportoRichiesto(rs.getBigDecimal(Elaborazione.Campi.IMPORTO_RICHIESTO));
					list.add(ela);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero del dettaglio delle elaborazioni");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		logger.info("trovati elementi: " + (list == null ? "0" : list.size()));
		return list;
	}

	public boolean updateLastView(String cf) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement("IF EXISTS (select * from puc001.TRK_LastView cf where cf=?) "
					+ "update puc001.TRK_LastView set lastView=CURRENT_TIMESTAMP  where cf=? " + "ELSE "
					+ "INSERT into  puc001.TRK_LastView (cf,lastView) values(?,CURRENT_TIMESTAMP) ");
			st.setString(1, cf.toUpperCase());
			st.setString(2, cf.toUpperCase());
			st.setString(3, cf.toUpperCase());

			resp = st.executeUpdate() > 0 ? true : false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nell'aggiornamento dell'ultima visita");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	public Timestamp getLastvieW(String cf) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		Timestamp resp = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement("select * from  puc001.TRK_LastView where cf=?");
			st.setString(1, cf.toUpperCase());

			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					resp = rs.getTimestamp("lastView");
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero dell'ultima visita");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	public List<ElencoStati> getElencoStati() throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		List<ElencoStati> resp = new ArrayList<ElencoStati>();
		ElencoStati elenco = null;
		TipoStato tipoStato = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement("select rs.*,ts.Stato as descrizione from  puc001.TRK_RelStatiOperazione as rs "
					+ "	join puc001.TRK_TipoStatoTracking as ts on ts.StatoID=rs.TipoStatoTrackingID");

			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					int idOpr = rs.getInt("TipoOperazioneTrackingID");
					int idStato = rs.getInt("TipoStatoTrackingID");
					int ordine = rs.getInt("Ordine");
					boolean isVisible = rs.getBoolean("isVisibile");
					String descrizione = rs.getString("descrizione");
					int index = getIndex(resp, idOpr);
					if (index == -1) {
						elenco = new ElencoStati();
						elenco.setIdOperazione(idOpr + "");
					} else {
						elenco = resp.get(index);
					}
					tipoStato = new TipoStato();
					tipoStato.setId(idStato);
					tipoStato.setPosizione(ordine);
					tipoStato.setVisibile(isVisible);
					tipoStato.setDescrizione(descrizione);
					elenco.getListaStati().add(tipoStato);
					resp.add(elenco);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero degli stati");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	private int getIndex(List<ElencoStati> resp, int idOpr) {
		if (resp.isEmpty())
			return -1;
		int index = 0;
		for (ElencoStati elencoStati : resp) {
			if (elencoStati.getIdOperazione().equals(idOpr + ""))
				return index;
			index++;
		}
		return -1;
	}

	public boolean sentDoc(String cf, String polizza, String utente, String nomeFile, String nomeUpload)
			throws Exception {
		logger.info(
				"aggiorno il db per doc inviato via ftp: cf " + cf + " polizza " + polizza + " nomefile: " + nomeFile);
		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement(
					"insert into puc001.TRK_DOCSENT (Contraente,polizza,nomeFile,fileUpload,utente) values(?,?,?,?,?)");

			st.setString(1, cf.toUpperCase());
			st.setString(2, polizza);
			st.setString(3, nomeFile);
			st.setString(4, nomeUpload);
			st.setString(5, utente);

			int executeUpdate = st.executeUpdate();
			if (executeUpdate > 0)
				resp = true;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nell'aggiornamento dei documenti inviati");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		logger.info("Db aggiornato correttamente");
		return resp;
	}

	public Agenda getAgenda(String cf) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		Agenda resp = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement(
					"select lv.lastView as data_ultimo_accesso,count(*) as tot_doc_movimentati from TRK_LastView  lv "
							+ "left join TRK_ElaborazioneTracking et on et.Contraente=lv.cf "
							+ "where lv.cf=? and et.DataRichiesta > lv.lastView " + "group by lv.lastView");
			st.setString(1, cf.toUpperCase());

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					resp = new Agenda();
					Date date = new SimpleDateFormat("yyyy-MM-dd")
							.parse(rs.getDate(Agenda.Campi.DATA_ULTIMO_ACCESSO).toString());
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					resp.setDataUltimoAccesso(sqlDate);
					resp.setNumeroNuoviMovimenti(rs.getInt(Agenda.Campi.TOT_DOC_MOVIMENTATI));

				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero dell'agenda");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	@Override
	public List<Beneficiario> listBen(String polizza) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		List<Beneficiario> resp = new ArrayList<Beneficiario>();
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement(
					"select bd.polizzaID,tcb.Descrizione as tipologia_beneficiario, tb.Descrizione,bd.* from BeneficiarioDettaglio  bd "
							+ "join TipoBeneficiario tb on bd.TipoBeneficiarioID=tb.id "
							+ "join TipoCasoBeneficiario tcb on tcb.ID=bd.TipoCasoBeneficiarioID "
							+ "where bd.PolizzaID=? " + "and getdate() between bd.DataInizio and bd.DataFine");
			st.setString(1, polizza);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				Beneficiario ben = null;
				while (rs.next()) {
					ben = new Beneficiario();
					ben.setCap(rs.getString(Beneficiario.CAMPI.CAP));
					ben.setCodiceFiscale_PartitaIVA(rs.getString(Beneficiario.CAMPI.CODICEFISCALE_PARTITAIVA));
					ben.setCognome_Denominazione(rs.getString(Beneficiario.CAMPI.COGNOME_DENOMINAZIONE));
					ben.setDataFine(rs.getDate(Beneficiario.CAMPI.DATAFINE));
					ben.setDataInizio(rs.getDate(Beneficiario.CAMPI.DATAINIZIO));
					ben.setDataNascita(rs.getDate(Beneficiario.CAMPI.DATANASCITA));
					ben.setDescrizione(rs.getString(Beneficiario.CAMPI.DESCRIZIONE));
					ben.setDiscriminator(rs.getString(Beneficiario.CAMPI.DISCRIMINATOR));
					ben.setiD(rs.getInt(Beneficiario.CAMPI.ID));
					ben.setIndirizzo(rs.getString(Beneficiario.CAMPI.INDIRIZZO));
					ben.setLocalita(rs.getString(Beneficiario.CAMPI.LOCALITA));
					ben.setLocalitaNascita(rs.getString(Beneficiario.CAMPI.LOCALITANASCITA));
					ben.setNazione(rs.getString(Beneficiario.CAMPI.NAZIONE));
					ben.setNazioneNascita(rs.getString(Beneficiario.CAMPI.NAZIONENASCITA));
					ben.setNome(rs.getString(Beneficiario.CAMPI.NOME));
					ben.setPercentualeBeneficio(rs.getDouble(Beneficiario.CAMPI.PERCENTUALEBENEFICIO));
					ben.setPolizzaID(rs.getString(Beneficiario.CAMPI.POLIZZAID));
					ben.setProvincia(rs.getString(Beneficiario.CAMPI.PROVINCIA));
					ben.setSesso(rs.getString(Beneficiario.CAMPI.SESSO));
//					ben.setTimestamp(timestamp);
					ben.setTipoBeneficiarioID(rs.getString(Beneficiario.CAMPI.TIPOCASOBENEFICIARIOID));
					ben.setTipoCasoBeneficiarioID(rs.getString(Beneficiario.CAMPI.TIPOCASOBENEFICIARIOID));
					ben.setTipologia_beneficiario(rs.getString(Beneficiario.CAMPI.TIPOLOGIA_BENEFICIARIO));
					ben.setTipoRelazioneBeneficiarioID_Assicurato(
							rs.getInt(Beneficiario.CAMPI.TIPORELAZIONEBENEFICIARIOID_ASSICURATO));
					ben.setTipoRelazioneBeneficiarioID_Contraente(
							rs.getInt(Beneficiario.CAMPI.TIPORELAZIONEBENEFICIARIOID_CONTRAENTE));
					resp.add(ben);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero dei beneficiari");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	@Override
	public List<ReferenteTerzo> listRefTer(String polizza) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		List<ReferenteTerzo> resp = new ArrayList<ReferenteTerzo>();
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement("select * from ReferenteAmministrativo r " + "where polizzaID=? "
					+ "and getdate() between r.DataInizio and r.DataFine");
			st.setString(1, polizza);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				ReferenteTerzo ref = null;
				while (rs.next()) {
					ref = new ReferenteTerzo();
					ref.setCap(rs.getString(Beneficiario.CAMPI.CAP));
					ref.setCodFiscale(rs.getString(ReferenteTerzo.CAMPI.CODFISCALE));
					ref.setCognome(rs.getString(ReferenteTerzo.CAMPI.COGNOME));
					ref.setDataFine(rs.getDate(Beneficiario.CAMPI.DATAFINE));
					ref.setDataInizio(rs.getDate(Beneficiario.CAMPI.DATAINIZIO));
					ref.setIndirizzo(rs.getString(Beneficiario.CAMPI.INDIRIZZO));
					ref.setLocalita(rs.getString(Beneficiario.CAMPI.LOCALITA));
					ref.setNazione(rs.getString(Beneficiario.CAMPI.NAZIONE));
					ref.setNome(rs.getString(Beneficiario.CAMPI.NOME));
					ref.setNumReferente(rs.getInt(ReferenteTerzo.CAMPI.NUMREFERENTE));
					ref.setPolizzaID(rs.getString(Beneficiario.CAMPI.POLIZZAID));
					ref.setProvincia(rs.getString(Beneficiario.CAMPI.PROVINCIA));
					ref.settIMESTAMP(rs.getDate(ReferenteTerzo.CAMPI.TIMESTAMP));
					resp.add(ref);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nel recupero dei referenti");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	@Override
	public boolean updateRef(ReferenteTerzo ref, String tipoModifica, String utente) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement(
					"INSERT into  puc001.TRK_ReferenteHistory (numReferente,tipo_modifica,utente_mod) values(?,?,?)");
			st.setInt(1, ref.getNumReferente());
			st.setString(2, tipoModifica);
			st.setString(3, utente);

			resp = st.executeUpdate() > 0 ? true : false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nell'aggiornamento dei referenti");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	@Override
	public boolean updateBen(Beneficiario ben, String tipoModifica, String utente) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));
			st = conn.prepareStatement(
					"INSERT into  puc001.TRK_BeneficiarioHistory (id_beneficiario,tipo_modifica,utente_mod) values(?,?,?)");
			st.setInt(1, ben.getiD());
			st.setString(2, tipoModifica);
			st.setString(3, utente);

			resp = st.executeUpdate() > 0 ? true : false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nell'aggiornamento dei beneficiari");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}
		return resp;
	}

	@Override
	public boolean aggiungiReferenteTerzo(String codiceFiscaleReferente, Integer numeroReferente,
			String cognomeReferente, String nomeReferente, String indirizzoReferente, String localitaReferente,
			String provinciaReferente, String capReferente, String nazioneReferente, String polizza, String userID,
			String parametro) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		// inserisco su puc
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn.prepareStatement(
					"insert into ReferenteAmministrativo (polizzaID, numReferente, dataInizio, dataFine, codFiscale, cognome, nome, indirizzo, cap,  localita, provincia, nazione, TIMESTAMP) values (?, ?, getDate(), '2050-12-31 00:00:00', ?, ?, ?, ?, ?, ? ,?,?, getDate())");

			st.setString(1, polizza);
			st.setInt(2, numeroReferente);
			st.setString(3, codiceFiscaleReferente);
			st.setString(4, cognomeReferente);
			st.setString(5, nomeReferente);
			st.setString(6, indirizzoReferente);
			st.setString(7, capReferente);
			st.setString(8, localitaReferente);
			st.setString(9, provinciaReferente);
			st.setString(10, nazioneReferente);

			resp = st.executeUpdate() > 0 ? true : false;
			if (resp)
				logger.info("INSERITO su PUC referente " + cognomeReferente + " " + nomeReferente + " per la polizza "
						+ polizza);
			else
				logger.info("NON INSERITO PUC referente " + cognomeReferente + " " + nomeReferente + " per la polizza "
						+ polizza);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nell' inserimento del referente");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;
	}

	@Override
	public boolean revocaReferenteTerzo(String codiceFiscaleReferente, Integer numeroReferente, String cognomeReferente,
			String nomeReferente, String indirizzoReferente, String localitaReferente, String provinciaReferente,
			String capReferente, String nazioneReferente, String polizza, String userID, String parametro)
			throws Exception {

		Connection conn = null;
		PreparedStatement st = null;
		boolean resp = false;
		// inserisco su puc
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn.prepareStatement(
					"update ReferenteAmministrativo set dataFine=getDate() where polizzaID = ? and dataFine='2050-12-31 00:00:00'");

			st.setString(1, polizza);

			resp = st.executeUpdate() > 0 ? true : false;
			if (resp)
				logger.info("REVOCATO su PUC il referente " + cognomeReferente + " " + nomeReferente
						+ " per la polizza " + polizza);
			else
				logger.info("NON REVOCATO su PUC il referente " + cognomeReferente + " " + nomeReferente
						+ " per la polizza " + polizza);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception("Errore nella revoca del referente");
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;

	}

	@Override
	public Soggetto getAssicurato(String polizza) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		Soggetto resp = null;
		// inserisco su puc
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn
					.prepareStatement("select p.ID,p.contraenteID,a.Nome,a.Cognome,a.ragioneSociale from polizza p \r\n"
							+ "	join Anagrafica a on a.ID=p.contraenteID\r\n"
							+ "	where trim(p.contraenteID)!='' and p.ID=?");

			st.setString(1, polizza);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				resp = new Soggetto();
				while (rs.next()) {
					resp.setNome(rs.getString(Soggetto.Campi.NOME));
					resp.setCognome(rs.getString(Soggetto.Campi.COGNOME));
					resp.setRagioneSociale(rs.getString(Soggetto.Campi.RAGIONE_SOCIALE));
					resp.setTipo("assicurato");
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;
	}

	@Override
	public Soggetto getContraente(String polizza) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		Soggetto resp = null;
		// inserisco su puc
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn.prepareStatement(
					"select p.ID,p.primoAssicuratoID,a.Nome,a.Cognome,a.ragioneSociale from polizza p \r\n"
							+ "	join Anagrafica a on a.ID=p.primoAssicuratoID\r\n"
							+ "	where trim(p.primoAssicuratoID)!=''and p.ID=?");

			st.setString(1, polizza);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				resp = new Soggetto();
				while (rs.next()) {
					resp.setNome(rs.getString(Soggetto.Campi.NOME));
					resp.setCognome(rs.getString(Soggetto.Campi.COGNOME));
					resp.setRagioneSociale(rs.getString(Soggetto.Campi.RAGIONE_SOCIALE));
					resp.setTipo("contraente");
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;
	}

	@Override
	public int geDocInviati(String cf, String polizza) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		int resp = 0;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn.prepareStatement("select * from puc001.TRK_DOCSENT where contraente=? and polizza=?");

			st.setString(1, cf);
			st.setString(2, polizza);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
//					TO DO 
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;
	}

	@Override
	public Pagamento getPagamento(String cf, String polizza, String numeroPratica) throws Exception {
		Connection conn = null;
		PreparedStatement st = null;
		Pagamento resp = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PUC_URL), prop.getProperty(Constant.PUC_USER),
					prop.getProperty(Constant.PUC_PWD),prop.getProperty(Constant.PUC_CLASS_NAME));

			st = conn.prepareStatement("select Sum(ImportoNetto) ImportoNetto from puc001.Pagamento  where NumeroPolizza=? and numeroPratica=? and anagraficaID=? ");

			st.setString(1, polizza);
			st.setString(2, numeroPratica);
			st.setString(3, cf);

			boolean execute = st.execute();
			if (execute) {
				ResultSet rs = st.getResultSet();
				resp=new Pagamento();
				while (rs.next()) {
					resp.setImportoRichiesto(rs.getBigDecimal(Pagamento.Campi.ImportoNetto));
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new Exception("Connessione al DB non riuscita");
			}
		}

		return resp;
	}

}
