/**
 * 11/27/2026 Class created.
 */

package com.ars.vmt.DTO;

import java.time.Instant;

public class SessionDTO {
    private String sessionId;
    private long userId;
    private Instant issuedTs;
    private Instant lastActTs;
    private Instant expTs;

    public SessionDTO (String sessionId, long userId, Instant issuedTs, Instant lastActTs, Instant expTs) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.issuedTs = issuedTs;
        this.lastActTs = lastActTs;
        this.expTs = expTs;
    }

    public SessionDTO () {
        this.sessionId = null;
        this.userId = -1;
        this.issuedTs = Instant.now();
        this.lastActTs = Instant.now();
        this.expTs = Instant.now();
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public long getUserId() {
        return this.userId;
    }

    public Instant getIssuedTs() {
        return this.issuedTs;
    }

    public Instant getLastActTs() {
        return this.lastActTs;
    }

    public Instant getExpTs() {
        return this.expTs;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setIssuedTs(Instant issuedTs) {
        this.issuedTs = issuedTs;
    }

    public void setLastActTs(Instant lastActTs) {
        this.lastActTs = lastActTs;
    }

    public void setExpTs(Instant expTs) {
        this.expTs = expTs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Hashed Session ID: "); sb.append(this.sessionId);
        sb.append("\nUser ID: "); sb.append(this.userId);
        sb.append("\nIssued TS: "); sb.append(this.issuedTs);
        sb.append("\nLast Active TS: "); sb.append(this.lastActTs);
        sb.append("\nExpiration TS: "); sb.append(this.expTs);

        return sb.toString();
    }

}
