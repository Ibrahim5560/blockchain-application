import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClientAccount } from 'app/shared/model/client-account.model';
import { getEntity, updateEntity, createEntity, reset } from './client-account.reducer';

export const ClientAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clientAccountEntity = useAppSelector(state => state.clientAccount.entity);
  const loading = useAppSelector(state => state.clientAccount.loading);
  const updating = useAppSelector(state => state.clientAccount.updating);
  const updateSuccess = useAppSelector(state => state.clientAccount.updateSuccess);

  const handleClose = () => {
    navigate('/client-account' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.balance !== undefined && typeof values.balance !== 'number') {
      values.balance = Number(values.balance);
    }

    const entity = {
      ...clientAccountEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...clientAccountEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blockchainApplicationApp.clientAccount.home.createOrEditLabel" data-cy="ClientAccountCreateUpdateHeading">
            <Translate contentKey="blockchainApplicationApp.clientAccount.home.createOrEditLabel">Create or edit a ClientAccount</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="client-account-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('blockchainApplicationApp.clientAccount.accountNumber')}
                id="client-account-accountNumber"
                name="accountNumber"
                data-cy="accountNumber"
                type="text"
              />
              <ValidatedField
                label={translate('blockchainApplicationApp.clientAccount.owner')}
                id="client-account-owner"
                name="owner"
                data-cy="owner"
                type="text"
              />
              <ValidatedField
                label={translate('blockchainApplicationApp.clientAccount.balance')}
                id="client-account-balance"
                name="balance"
                data-cy="balance"
                type="text"
              />
              <ValidatedField
                label={translate('blockchainApplicationApp.clientAccount.creationDate')}
                id="client-account-creationDate"
                name="creationDate"
                data-cy="creationDate"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client-account" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ClientAccountUpdate;
