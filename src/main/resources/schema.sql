CREATE SEQUENCE IF NOT EXISTS post_id_seq START WITH 1 INCREMENT BY 10;

CREATE TABLE IF NOT EXISTS posts 
(
  id          INTEGER PRIMARY KEY DEFAULT nextval('post_id_seq'),
  title       TEXT NOT NULL,
  content     TEXT NOT NULL,
  is_published BOOLEAN NOT NULL DEFAULT FALSE,
  created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS event_publication
(
  id               UUID NOT NULL,
  listener_id      TEXT NOT NULL,
  event_type       TEXT NOT NULL,
  serialized_event TEXT NOT NULL,
  publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
  completion_date  TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS event_publication_serialized_event_hash_idx ON event_publication USING hash(serialized_event);
CREATE INDEX IF NOT EXISTS event_publication_by_completion_date_idx ON event_publication (completion_date);


CREATE TABLE IF NOT EXISTS shedlock
(
  name VARCHAR(64) NOT NULL, 
  lock_until TIMESTAMP NOT NULL,
  locked_at TIMESTAMP NOT NULL, 
  locked_by VARCHAR(255) NOT NULL, 
  PRIMARY KEY (name)
);

/*
  -- SQL for acquiring a lock in ShedLock
  INSERT INTO shedlock
  (
    name, 
    lock_until, 
    locked_at, 
    locked_by
  ) 
  VALUES
  (
    ?, 
    timezone('utc', CURRENT_TIMESTAMP) + cast(? as interval), 
    timezone('utc', CURRENT_TIMESTAMP), 
    ?
  ) 
  ON CONFLICT (name) DO UPDATE SET 
    lock_until = timezone('utc', CURRENT_TIMESTAMP) + cast(? as interval), 
    locked_at = timezone('utc', CURRENT_TIMESTAMP), 
    locked_by = ? 
  WHERE 
    shedlock.name = ? AND shedlock.lock_until <= timezone('utc', CURRENT_TIMESTAMP)
*/