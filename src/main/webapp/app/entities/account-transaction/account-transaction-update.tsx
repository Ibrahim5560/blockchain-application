import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClientAccount } from 'app/shared/model/client-account.model';
import { getEntities as getClientAccounts } from 'app/entities/client-account/client-account.reducer';
import { IAccountTransaction } from 'app/shared/model/account-transaction.model';
import { getEntity, updateEntity, createEntity, reset } from './account-transaction.reducer';

export const AccountTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clientAccounts = useAppSelector(state => state.clientAccount.entities);
  const accountTransactionEntity = useAppSelector(state => state.accountTransaction.entity);
  const loading = useAppSelector(state => state.accountTransaction.loading);
  const updating = useAppSelector(state => state.accountTransaction.updating);
  const updateSuccess = useAppSelector(state => state.accountTransaction.updateSuccess);

  const handleClose = () => {
    navigate('/account-transaction' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClientAccounts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }

    const entity = {
      ...accountTransactionEntity,
      ...values,
      clientAccount: clientAccounts.find(it => it.id.toString() === values.clientAccount.toString()),
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
          ...accountTransactionEntity,
          clientAccount: accountTransactionEntity?.clientAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blockchainApplicationApp.accountTransaction.home.createOrEditLabel" data-cy="AccountTransactionCreateUpdateHeading">
            <Translate contentKey="blockchainApplicationApp.accountTransaction.home.createOrEditLabel">
              Create or edit a AccountTransaction
            </Translate>
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
                  id="account-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('blockchainApplicationApp.accountTransaction.transactionName')}
                id="account-transaction-transactionName"
                name="transactionName"
                data-cy="transactionName"
                type="text"
              />
              <ValidatedField
                label={translate('blockchainApplicationApp.accountTransaction.amount')}
                id="account-transaction-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                id="account-transaction-clientAccount"
                name="clientAccount"
                data-cy="clientAccount"
                label={translate('blockchainApplicationApp.accountTransaction.clientAccount')}
                type="select"
              >
                <option value="" key="0" />
                {clientAccounts
                  ? clientAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/account-transaction" replace color="info">
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

export default AccountTransactionUpdate;
