-- Add pronunciation and audio_url to vocabulary_items
ALTER TABLE vocabulary_items
  ADD COLUMN pronunciation TEXT,
  ADD COLUMN audio_url TEXT;

-- Add index for normalized_term if not exists (helps lookups)
CREATE INDEX IF NOT EXISTS idx_vocabulary_items_normalized_term ON vocabulary_items(normalized_term);
