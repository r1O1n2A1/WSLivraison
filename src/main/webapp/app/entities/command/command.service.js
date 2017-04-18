(function() {
    'use strict';
    angular
        .module('wsLivraisonApp')
        .factory('Command', Command);

    Command.$inject = ['$resource', 'DateUtils'];

    function Command ($resource, DateUtils) {
        var resourceUrl =  'api/commands/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOrder = DateUtils.convertDateTimeFromServer(data.dateOrder);
                        data.dateTaken = DateUtils.convertDateTimeFromServer(data.dateTaken);
                        data.dateShipping = DateUtils.convertDateTimeFromServer(data.dateShipping);
                        data.dateDelivery = DateUtils.convertDateTimeFromServer(data.dateDelivery);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
