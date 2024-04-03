CREATE OR REPLACE FUNCTION insert_pessoa(
    p_apelido VARCHAR(32), p_nome VARCHAR(100), p_nascimento VARCHAR(15), p_stack VARCHAR(300), p_campo_query VARCHAR(300))
RETURNS TABLE(user_id UUID) AS $$
DECLARE
    novo_user_id UUID;
BEGIN

    IF EXISTS (
        SELECT 1 FROM pessoas 
        WHERE apelido = p_apelido) THEN
            RAISE EXCEPTION 'USER_ALREADY_EXISTS';
        END IF;

    INSERT INTO pessoas (apelido, nome, nascimento, stack , campo_query)
    VALUES (p_apelido, p_nome, p_nascimento, p_stack, p_campo_query)
    RETURNING id INTO novo_user_id;

    RETURN QUERY SELECT novo_user_id;
END;

$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION select_by_id(user_id UUID)
RETURNS TABLE(id UUID, apelido VARCHAR, nome VARCHAR, nascimento VARCHAR, stack VARCHAR) AS $$

BEGIN
	IF NOT EXISTS (
		SELECT 1 FROM pessoas p
		WHERE p.id = user_id) THEN 
			RAISE EXCEPTION 'USER_NOT_FOUND';
	END IF;

	RETURN QUERY 
	SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack
	FROM pessoas p
	WHERE p.id = user_id;
END;

$$ LANGUAGE plpgsql;


-- 
CREATE OR REPLACE FUNCTION select_by_t(query VARCHAR(300))
RETURNS TABLE(id UUID, apelido VARCHAR, nome VARCHAR, nascimento VARCHAR, stack VARCHAR) AS $$

BEGIN
	RETURN QUERY 
	SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack
	FROM pessoas p
	WHERE LOWER(p.campo_query) LIKE LOWER(CONCAT('%', query, '%'));
END;

$$ LANGUAGE plpgsql;
