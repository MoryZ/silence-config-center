# Crypto Compatibility Plan

## Background

`AESUtils.encrypt` has been upgraded from fixed-IV CBC output to random-IV output.
To reduce integration ambiguity, ciphertext is now versioned.

## Ciphertext Formats

- Legacy format (v1):
  - `cipherHex`
  - No version prefix
  - IV was fixed in old implementation

- Current format (v2):
  - `02 + ivHex + cipherHex`
  - `02` is the version prefix
  - `ivHex` is 32 hex chars (16 bytes)

## API in AESUtils

- `encrypt(plainText, appKey)`
  - Produces v2 ciphertext

- `isV2CipherText(cipherText)`
  - Detects whether a ciphertext is v2

- `extractV2IvHex(cipherText)`
  - Extracts IV hex from v2 ciphertext

- `extractV2CipherHex(cipherText)`
  - Extracts encrypted payload hex from v2 ciphertext

## Compatibility Guidance

1. If downstream systems only store/transmit ciphertext:
- No action needed except accepting longer hex strings.

2. If downstream systems parse/decrypt ciphertext:
- Add branch logic:
- If `isV2CipherText` then parse `ivHex + cipherHex`.
- Else treat as legacy v1 payload.

3. Migration suggestion:
- Keep read compatibility for v1.
- Rewrite old records to v2 during normal update flow or by batch migration.

## Rollout Checklist

1. Validate DB column length can hold v2 payload.
2. Validate any gateway/consumer does not assume fixed ciphertext length.
3. Add integration test coverage for both v1 and v2 payload handling.
